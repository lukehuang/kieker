package kieker.tpmon.asyncFsWriter;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.AbstractMonitoringDataWriter;
import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;

import kieker.tpmon.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author matthias
 */
public class AsyncFsWriterProducer extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(AsyncFsWriterProducer.class);
    //configuration parameter
    final int numberOfFsWriters = 1; // one is usually sufficient and more usuable since only one file is created at once
    //internal variables
    private Vector<Worker> workers = new Vector<Worker>();
    private BlockingQueue<KiekerExecutionRecord> blockingQueue = null;
    private String filenamePrefix = null;
    private final static String defaultConstructionErrorMsg =
            "Do not select this writer using the full-qualified classname. " +
            "Use the the constant " + TpmonController.WRITER_ASYNCFS +
            " and the file system specific configuration properties.";

    public AsyncFsWriterProducer() {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }

    @TpmonInternal
    public boolean init(String initString) {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }

    @TpmonInternal
    public Vector<Worker> getWorkers() {
        return workers;
    }

    public AsyncFsWriterProducer(String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        this.init();
    }

    @TpmonInternal
    public void init() {
        blockingQueue = new ArrayBlockingQueue<KiekerExecutionRecord>(8000);
        for (int i = 0; i < numberOfFsWriters; i++) {
            AsyncFsWriterWorker dbw = new AsyncFsWriterWorker(blockingQueue, filenamePrefix);
            new Thread(dbw).start();
            workers.add(dbw);
        }
        //System.out.println(">Kieker-Tpmon: (" + numberOfFsWriters + " threads) will write to the file system");
        log.info(">Kieker-Tpmon: (" + numberOfFsWriters + " threads) will write to the file system");
    }

    /**
     * This method is not synchronized, in contrast to the insert method of the Dbconnector.java.
     * It uses several dbconnections in parallel using the consumer, producer pattern.
     *
     */
    @TpmonInternal
    public boolean insertMonitoringDataNow(KiekerExecutionRecord execData) {
        if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncFsWriterDispatcher.insertMonitoringDataNow");
        }

        try {
            blockingQueue.add(execData); // tries to add immediately!
        //System.out.println(""+blockingQueue.size());

        } catch (Exception ex) {
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " insertMonitoringData() failed: Exception: " + ex);
            return false;
        }
        return true;
    }

    @TpmonInternal()
    public String getFilenamePrefix() {
        return filenamePrefix;
    }
    
  @TpmonInternal()
    public String getInfoString() {
        return new String ("filenamePrefix :" + filenamePrefix);
    }
}

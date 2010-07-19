package kieker.analysis.datamodel;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2009 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;
import kieker.common.util.LoggingTimestampConverter;
import kieker.analysis.plugin.traceAnalysis.traceReconstruction.InvalidTraceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Andre van Hoorn
 */
public class ExecutionTrace extends Trace {

    private static final Log log = LogFactory.getLog(ExecutionTrace.class);

    private final AtomicReference<MessageTrace> messageTrace = new AtomicReference<MessageTrace>();

    private final SortedSet<Execution> set = new TreeSet<Execution>(new Comparator<Execution>() {
        public int compare(Execution e1, Execution e2) {
            if (e1.getTraceId() == e2.getTraceId()) {
                if (e1.getEoi() < e2.getEoi()) {
                    return -1;
                }
                if (e1.getEoi() > e2.getEoi()) {
                    return 1;
                }
                return 0;
            } else {
                if (e1.getTin() < e2.getTin()) {
                    return -1;
                }
                if (e1.getTin() > e2.getTin()) {
                    return 1;
                }
                return 0;
            }
        }
    });
    private long minTin = Long.MAX_VALUE;
    private long maxTout = Long.MIN_VALUE;
    private int maxStackDepth = -1;

    public ExecutionTrace(final long traceId) {
        super(traceId);
    }

    /**
     * Adds an execution to the trace.
     *
     * @param execution
     * @throws InvalidTraceException if the traceId of the passed Execution
     *         object is not the same as the traceId of this ExecutionTrace object.
     */
    public void add(Execution execution) throws InvalidTraceException {
        if (this.getTraceId() != execution.getTraceId()) {
            throw new InvalidTraceException("TraceId of new record (" + execution.getTraceId() + ") differs from Id of this trace (" + this.getTraceId() + ")");
        }
        if (execution.getTin() < this.minTin) {
            this.minTin = execution.getTin();
        }
        if (execution.getTout() > this.maxTout) {
            this.maxTout = execution.getTout();
        }
        if (execution.getEss() > this.maxStackDepth) {
            this.maxStackDepth = execution.getEss();
        }
        this.set.add(execution);
    }

    /**
     * Returns the message trace representation for this trace.
     * The transformation to a message trace is only computed during the
     * first execution of this method. After this, the stored reference
     * is returned.
     */
    public synchronized MessageTrace toMessageTrace(final Execution rootExecution)
            throws InvalidTraceException {
        MessageTrace mt = this.messageTrace.get();
        if (mt != null){
            return mt;
        }

        Vector<Message> mSeq = new Vector<Message>();
        Stack<Message> curStack = new Stack<Message>();
        Iterator<Execution> eSeqIt = this.set.iterator();

        Execution prevE = rootExecution;
        int prevEoi = -1;
        for (int itNum = 0; eSeqIt.hasNext(); itNum++) {
            Execution curE = eSeqIt.next();
            if (itNum++ == 0 && curE.getEss() != 0) {
                InvalidTraceException ex =
                        new InvalidTraceException("First execution must have ess "
                        + "0 (found " + curE.getEss() + ")\n Causing execution: " + curE);
                log.fatal("Found invalid trace", ex);
                throw ex;
            }
            if (prevEoi != curE.getEoi() - 1) {
                InvalidTraceException ex =
                        new InvalidTraceException("Eois must increment by 1 --"
                        + "but found sequence <" + prevEoi + "," + curE.getEoi() + ">" + "(Execution: " + curE + ")");
                log.fatal("Found invalid trace", ex);
                throw ex;
            }
            prevEoi = curE.getEoi();

            // First, we might need to clean up the stack for the next execution
            // callMessage
            if (prevE != rootExecution && prevE.getEss() >= curE.getEss()) {
                Execution curReturnReceiver; // receiverComponentName of return message
                while (curStack.size() > curE.getEss()) {
                    Message poppedCall = curStack.pop();
                    prevE = poppedCall.getReceivingExecution(); //.execution;
                    curReturnReceiver = poppedCall.getSendingExecution(); //curStack.peek().getSendingExecution(); //.execution;
                    Message m = new SynchronousReplyMessage(prevE.getTout(),
                            prevE, curReturnReceiver);
                    mSeq.add(m);
                    prevE = curReturnReceiver;
                }
            }
            // Now, we handle the current execution callMessage 
            if (prevE == rootExecution) { // initial execution callMessage
                Message m = new SynchronousCallMessage(curE.getTin(), rootExecution, curE);
                mSeq.add(m);
                curStack.push(m);
            } else if (prevE.getEss()+1 == curE.getEss()) { // usual callMessage with senderComponentName and receiverComponentName
                Message m = new SynchronousCallMessage(curE.getTin(), prevE, curE);
                mSeq.add(m);
                curStack.push(m);
            } else if (prevE.getEss() < curE.getEss()){ // detect ess incrementation by > 1
                InvalidTraceException ex =
                        new InvalidTraceException("Ess are only allowed to increment by 1 --"
                        + "but found sequence <" + prevE.getEss() + "," + curE.getEss() + ">" + "(Execution: " + curE + ")");
                log.fatal("Found invalid trace", ex);
                throw ex;
            }
            if (!eSeqIt.hasNext()) { // empty stack completely, since no more executions
                Execution curReturnReceiver; // receiverComponentName of return message
                while (!curStack.empty()) {
                    Message poppedCall = curStack.pop();
                    prevE = poppedCall.getReceivingExecution(); //.execution;
                    curReturnReceiver = poppedCall.getSendingExecution(); //curStack.peek().getSendingExecution(); //.execution;
                    Message m = new SynchronousReplyMessage(prevE.getTout(),
                            prevE, curReturnReceiver);
                    mSeq.add(m);
                    prevE = curReturnReceiver;
                }
            }
            prevE = curE; // prepair next loop
        }
        mt = new MessageTrace(this.getTraceId(), mSeq);
        return mt;
    }

    public final SortedSet<Execution> getTraceAsSortedSet() {
        return this.set;
    }

    /**
     * Returns the length of this trace in terms of the number of contained
     * executions.
     *
     * @return
     */
    public final int getLength() {
        return this.set.size();
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder("TraceId " + this.getTraceId()).append(" (minTin=").append(this.minTin).append(" (").append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.minTin)).append(")").append("; maxTout=").append(this.maxTout).append(" (").append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.maxTout)).append(")").append("; maxStackDepth=").append(this.maxStackDepth).append("):\n");
        for (Execution e : this.set) {
            strBuild.append("<");
            strBuild.append(e.toString()).append(">\n");
        }
        return strBuild.toString();
    }

    /**
     * Returns the maximum step depth within the trace.
     *
     * @return
     */
    public int getMaxStackDepth() {
        return this.maxStackDepth;
    }

    /**
     * Returns the maximum timestamp value of an execution return in this trace.
     *
     * Notice that you should need use this value to reason about the
     * control flow --- particularly in distributed scenarios.
     *
     * @return
     */
    public long getMaxTout() {
        return this.maxTout;
    }

    /**
     * Returns the minimum timestamp of an execution start in this trace.
     *
     * Notice that you should need use this value to reason about the
     * control flow --- particularly in distributed scenarios.
     *
     * @return
     */
    public long getMinTin() {
        return this.minTin;
    }
}

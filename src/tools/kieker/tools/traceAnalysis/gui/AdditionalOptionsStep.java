/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
 ***************************************************************************/

package kieker.tools.traceAnalysis.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import kieker.tools.traceAnalysis.Constants;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class AdditionalOptionsStep extends AbstractStep {

	private static final long serialVersionUID = 1L;

	private static final String PROPERTY_KEY_IDENTIFIER = AdditionalOptionsStep.class.getSimpleName();
	private static final String PROPERTY_KEY_VERBOSE = PROPERTY_KEY_IDENTIFIER + ".verbose";
	private static final String PROPERTY_KEY_IGNORE_INVALID_TRACES = PROPERTY_KEY_IDENTIFIER + ".ignoreInvalidTraces";
	private static final String PROPERTY_KEY_IGNORE_ASSUMED_CALLS = PROPERTY_KEY_IDENTIFIER + ".ignoreAssumedCalls";
	private static final String PROPERTY_KEY_USE_SHORT_LABELS = PROPERTY_KEY_IDENTIFIER + ".useShortLabels";
	private static final String PROPERTY_KEY_INCLUDE_SELF_LOOPS = PROPERTY_KEY_IDENTIFIER + ".includeSelfLoops";
	private static final String PROPERTY_KEY_MAX_TRACE_DURATION = PROPERTY_KEY_IDENTIFIER + ".maxTraceDurationMS";
	private static final String PROPERTY_KEY_MAX_TRACE_DURATION_INPUT = PROPERTY_KEY_IDENTIFIER + ".maxTraceDurationMSInput";
	private static final String PROPERTY_KEY_TRACE_COLORING_MAP = PROPERTY_KEY_IDENTIFIER + ".traceColoringMap";
	private static final String PROPERTY_KEY_TRACE_COLORING_MAP_INPUT = PROPERTY_KEY_IDENTIFIER + ".traceColoringMapInput";
	private static final String PROPERTY_KEY_DESCRIPTION = PROPERTY_KEY_IDENTIFIER + ".description";
	private static final String PROPERTY_KEY_DESCRIPTION_INPUT = PROPERTY_KEY_IDENTIFIER + ".descriptionInput";

	private final JLabel infoLabel = new JLabel("<html>In this step you manage additional options for the trace analysis.</html>");
	private final JPanel expandingPanel = new JPanel();
	private final JCheckBox verbose = new JCheckBox("Verbosely list used parameters and processed traces");
	private final JCheckBox ignoreInvalidTraces = new JCheckBox("Ignore Invalid Traces");
	private final JCheckBox ignoreAssumedCalls = new JCheckBox("Draw Assumed Calls As Usual Calls");
	private final JCheckBox useShortLabels = new JCheckBox("Use Short Labels");
	private final JCheckBox includeSelfLoops = new JCheckBox("Include Self Loops");
	private final JCheckBox maxTraceDurationMS = new JCheckBox("Maximal Duration of Traces in Milliseconds:");
	private final JSpinner maxTraceDurationMSInput = new JSpinner();
	private final JCheckBox traceColoringMap = new JCheckBox("Use Trace Coloring Map File:");
	private final JTextField traceColoringMapInput = new JTextField(".");
	private final JButton traceColoringMapChooseButton = new JButton("Choose");
	private final JCheckBox description = new JCheckBox("Use Description File:");
	private final JTextField descriptionInput = new JTextField(".");
	private final JButton descriptionChooseButton = new JButton("Choose");

	public AdditionalOptionsStep() {
		this.addAndLayoutComponents();
		this.addLogicToComponents();
	}

	private void addAndLayoutComponents() {
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.infoLabel, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(5, 5, 0, 0);
		this.add(this.verbose, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.ignoreAssumedCalls, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.ignoreInvalidTraces, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.useShortLabels, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets.set(0, 5, 0, 0);
		this.add(this.includeSelfLoops, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.maxTraceDurationMS, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(this.maxTraceDurationMSInput, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.traceColoringMap, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.traceColoringMapInput, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.traceColoringMapChooseButton, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		this.add(this.description, gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		this.add(this.descriptionInput, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets.set(5, 5, 5, 5);
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.descriptionChooseButton, gridBagConstraints);

		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		this.add(this.expandingPanel, gridBagConstraints);
	}

	@Override
	@SuppressWarnings("synthetic-access")
	public void addSelectedTraceAnalysisParameters(final Collection<String> parameters) {
		if (this.verbose.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_VERBOSE);
		}

		if (this.ignoreInvalidTraces.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES);
		}

		if (this.useShortLabels.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_SHORTLABELS);
		}

		if (this.includeSelfLoops.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_INCLUDESELFLOOPS);
		}

		if (this.maxTraceDurationMS.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_MAXTRACEDURATION);
			parameters.add(((Long) this.maxTraceDurationMSInput.getValue()).toString());
		}

		if (this.ignoreAssumedCalls.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_IGNORE_ASSUMED);
		}

		if (this.description.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_ADD_DESCRIPTIONS);
			parameters.add(this.descriptionInput.getText());
		}

		if (this.traceColoringMap.isSelected()) {
			parameters.add("--" + Constants.CMD_OPT_NAME_TRACE_COLORING);
			parameters.add(this.traceColoringMapInput.getText());
		}
	}

	private void addLogicToComponents() {
		this.descriptionChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(AdditionalOptionsStep.this.descriptionInput.getText());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				if (fileChooser.showOpenDialog(AdditionalOptionsStep.this) == JFileChooser.APPROVE_OPTION) {
					AdditionalOptionsStep.this.descriptionInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		this.traceColoringMapChooseButton.addActionListener(new ActionListener() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(final ActionEvent event) {
				final JFileChooser fileChooser = new JFileChooser(AdditionalOptionsStep.this.traceColoringMapInput.getText());
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				if (fileChooser.showOpenDialog(AdditionalOptionsStep.this) == JFileChooser.APPROVE_OPTION) {
					AdditionalOptionsStep.this.traceColoringMapInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}

	@Override
	public void loadDefaultConfiguration() {
		this.maxTraceDurationMSInput.setValue(600000);
	}

	@Override
	public void saveCurrentConfiguration(final Properties properties) {
		properties.setProperty(PROPERTY_KEY_VERBOSE, Boolean.toString(this.verbose.isSelected()));
		properties.setProperty(PROPERTY_KEY_IGNORE_INVALID_TRACES, Boolean.toString(this.ignoreInvalidTraces.isSelected()));
		properties.setProperty(PROPERTY_KEY_USE_SHORT_LABELS, Boolean.toString(this.useShortLabels.isSelected()));
		properties.setProperty(PROPERTY_KEY_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops.isSelected()));
		properties.setProperty(PROPERTY_KEY_MAX_TRACE_DURATION, Boolean.toString(this.maxTraceDurationMS.isSelected()));
		properties.setProperty(PROPERTY_KEY_MAX_TRACE_DURATION_INPUT, Integer.toString((Integer) this.maxTraceDurationMSInput.getValue()));
		properties.setProperty(PROPERTY_KEY_IGNORE_ASSUMED_CALLS, Boolean.toString(this.ignoreAssumedCalls.isSelected()));
		properties.setProperty(PROPERTY_KEY_TRACE_COLORING_MAP, Boolean.toString(this.traceColoringMap.isSelected()));
		properties.setProperty(PROPERTY_KEY_TRACE_COLORING_MAP_INPUT, this.traceColoringMapInput.getText());
		properties.setProperty(PROPERTY_KEY_DESCRIPTION, Boolean.toString(this.description.isSelected()));
		properties.setProperty(PROPERTY_KEY_DESCRIPTION_INPUT, this.descriptionInput.getText());
	}

	@Override
	public void loadCurrentConfiguration(final Properties properties) {
		this.verbose.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_VERBOSE)));
		this.ignoreInvalidTraces.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_IGNORE_INVALID_TRACES)));
		this.useShortLabels.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_USE_SHORT_LABELS)));
		this.includeSelfLoops.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_INCLUDE_SELF_LOOPS)));
		this.maxTraceDurationMS.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_MAX_TRACE_DURATION)));
		this.ignoreAssumedCalls.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_IGNORE_ASSUMED_CALLS)));
		this.traceColoringMap.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_TRACE_COLORING_MAP)));
		this.description.setSelected(Boolean.parseBoolean(properties.getProperty(PROPERTY_KEY_DESCRIPTION)));
		this.descriptionInput.setText(properties.getProperty(PROPERTY_KEY_DESCRIPTION_INPUT));
		this.traceColoringMapInput.setText(properties.getProperty(PROPERTY_KEY_TRACE_COLORING_MAP_INPUT));
		this.maxTraceDurationMSInput.setValue(Integer.parseInt(properties.getProperty(PROPERTY_KEY_MAX_TRACE_DURATION_INPUT)));
	}

	@Override
	public boolean isNextStepAllowed() {
		return true;
	}
}

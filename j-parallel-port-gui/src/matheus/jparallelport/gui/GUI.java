package matheus.jparallelport.gui;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class GUI extends MyComposite {

	// Data group components
	private Group dataGroup = null;
	private Bits dataGroupBits = new Bits();
	Text dataDecFieldText;
	Text dataHexFieldText;
	Button[] dataGroupButtons = new Button[8];

	// Status group components
	private Group statusGroup = null;
	private Bits statusGroupBits = new Bits((byte) 0x80, (byte) 0xff, 3);
	Text statusDecFieldText;
	Text statusHexFiledText;
	Text statusMaskedDecFieldText;
	Text statusMaskedHexFiledText;
	Button[] statusGroupButtons = new Button[8];

	// Control group components
	private Group controlGroup = null;
	private Bits controlGroupBits = new Bits((byte) 0x0b, (byte) 0x0f);
	Text controlDecFieldText;
	Text controlHexFiledText;
	Text controlMaskedDecFieldText;
	Text controlMaskedHexFiledText;
	Button[] controlGroupButtons = new Button[8];

	static Button createButton(Composite parent) {
		return new Button(parent, SWT.TOGGLE);
	}

	Image[] dataEnabledImages = new Image[8];
	Image[] statusEnabledImages = new Image[8];
	Image[] controlEnabledImages = new Image[8];

	Image[] dataDisabledImages = new Image[8];
	Image[] statusDisabledImages = new Image[8];
	Image[] controlDisabledImages = new Image[8];

	void loadButtonsImages() {
		for (int i = 0; i < 8; i++) {
			dataEnabledImages[i] = new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(
							"/matheus/jparallelport/gui/images/D"
									+ Integer.toString(i) + "-e.png"));
			dataDisabledImages[i] = new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(
							"/matheus/jparallelport/gui/images/D"
									+ Integer.toString(i) + "-d.png"));

			statusEnabledImages[i] = new Image(Display.getCurrent(), getClass()
					.getResourceAsStream(
							"/matheus/jparallelport/gui/images/S"
									+ Integer.toString(i) + "-e.png"));
			statusDisabledImages[i] = new Image(Display.getCurrent(),
					getClass().getResourceAsStream(
							"/matheus/jparallelport/gui/images/S"
									+ Integer.toString(i) + "-d.png"));

			controlEnabledImages[i] = new Image(Display.getCurrent(),
					getClass().getResourceAsStream(
							"/matheus/jparallelport/gui/images/C"
									+ Integer.toString(i) + "-e.png"));
			controlDisabledImages[i] = new Image(Display.getCurrent(),
					getClass().getResourceAsStream(
							"/matheus/jparallelport/gui/images/C"
									+ Integer.toString(i) + "-d.png"));
		}
	}

	void sendData() {
		try {
			Application.parport.writeData(dataGroupBits.toByte());
			Application.parport.writeControl(controlGroupBits.toByte());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	synchronized void retriveData() {
		try {
			dataGroupBits.setBits(Application.parport.readData());
			statusGroupBits.setBits(Application.parport.readStatus());
			controlGroupBits.setBits(Application.parport.readControl());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	/**
	 * Refresh the data group on user interface
	 */
	final void refreshDataGroupGUI() {
		// Data fields
		dataDecFieldText.setText(dataGroupBits.toDecString());
		dataHexFieldText.setText(dataGroupBits.toHexString());
		for (int i = 0; i < 8; i++) {
			// Data buttons
			if (dataGroupBits.data[i]) {
				dataGroupButtons[i].setImage(dataEnabledImages[i]);
				dataGroupButtons[i].setSelection(true);
			} else {
				dataGroupButtons[i].setImage(dataDisabledImages[i]);
				dataGroupButtons[i].setSelection(false);
			}
		}
	}

	/**
	 * Refresh the status group on user interface
	 */
	final void refreshStatusGroupGUI() {
		// Status fileds
		statusDecFieldText.setText(statusGroupBits.toDecString());
		statusHexFiledText.setText(statusGroupBits.toHexString());
		statusMaskedDecFieldText
				.setText(statusGroupBits.toDecStringMascarade());
		statusMaskedHexFiledText.setText(statusGroupBits.toHexStringMacarade());
		for (int i = 0; i < 8; i++) {
			// Status buttons
			if (statusGroupBits.data[i]) {
				statusGroupButtons[i].setImage(statusEnabledImages[i]);
				statusGroupButtons[i].setSelection(true);
			} else {
				statusGroupButtons[i].setImage(statusDisabledImages[i]);
				statusGroupButtons[i].setSelection(false);
			}
		}
	}

	/**
	 * Refresh the control group on user interface
	 */
	final void refreshControlGroupGUI() {
		// Controls fileds
		controlDecFieldText.setText(controlGroupBits.toDecString());
		controlHexFiledText.setText(controlGroupBits.toHexString());
		controlMaskedDecFieldText.setText(controlGroupBits
				.toDecStringMascarade());
		controlMaskedHexFiledText.setText(controlGroupBits
				.toHexStringMacarade());
		for (int i = 0; i < 8; i++) {
			// Control buttons
			if (controlGroupBits.data[i]) {
				controlGroupButtons[i].setImage(controlEnabledImages[i]);
				controlGroupButtons[i].setSelection(true);
			} else {
				controlGroupButtons[i].setImage(controlDisabledImages[i]);
				controlGroupButtons[i].setSelection(false);
			}
		}
	}

	// store previous state of each byte group and then just refresh
	// the gui where found changes
	byte dataGroupOldByte = (byte) 0, statusGroupOldByte = (byte) 0,
			controlGroupOldByte = (byte) 0;

	/**
	 * Refresh GUI interface
	 */
	final void refreshGUI() {
		byte aux;
		aux = dataGroupBits.toByte();
		// check for previous state in order to call refresh
		// operation
		if (aux != dataGroupOldByte) {
			dataGroupOldByte = aux;
			refreshDataGroupGUI();
		}

		aux = statusGroupBits.toByte();
		if (aux != statusGroupOldByte) {
			statusGroupOldByte = aux;
			refreshStatusGroupGUI();
		}

		aux = controlGroupBits.toByte();
		if (aux != controlGroupOldByte) {
			controlGroupOldByte = aux;
			refreshControlGroupGUI();
		}
	}

	/**
	 * Create the data group buttons
	 * 
	 * @param dataIndex
	 * @return
	 * @category data
	 */
	Button createDataGroupButton(final int dataIndex) {
		final Button button = createButton(dataGroup);
		button.setImage(dataDisabledImages[dataIndex]);

		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				dataGroupBits.data[dataIndex] = button.getSelection();
				sendData();
				refreshGUI();
			}
		});

		return button;
	}

	/**
	 * Create the status group buttons
	 * 
	 * @param dataIndex
	 * @return
	 * @category status
	 */
	Button createStatusGroupButton(final int dataIndex) {
		final Button button = createButton(statusGroup);
		button.setImage(statusDisabledImages[dataIndex]);

		// Prevent user to change the button state
		// (status bits is read-only)
		button.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				button.setSelection(!button.getSelection());
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {

			}
		});
		
		return button;
	}

	/**
	 * Create the control group buttons
	 * 
	 * @param dataIndex
	 * @return
	 * @category control
	 */
	Button createControlGroupButton(final int dataIndex) {
		final Button button = createButton(controlGroup);
		button.setImage(controlDisabledImages[dataIndex]);

		button.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				controlGroupBits.data[dataIndex] = button.getSelection();
				sendData();
				refreshGUI();
			}
		});

		return button;
	}

	static Text createText(Composite parent) {
		return new Text(parent, SWT.BORDER);
	}

	/**
	 * Create the data decimal text field
	 * 
	 * @return
	 * @category data
	 */
	Text createDecDataGroupText() {
		final Text text = createText(dataGroup);

		text.addListener(SWT.Verify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!e.text.matches("^-?[0-9]*")) {
					e.doit = false;
				}
			}
		});

		text.addListener(SWT.CHANGED, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				dataGroupBits.fromDecString(text.getText());
				sendData();

			}
		});

		return text;
	}

	/**
	 * Create the data hex text field
	 * 
	 * @return
	 * @category data
	 */
	Text createHexDataGroupText() {
		final Text text = createText(dataGroup);

		text.addListener(SWT.Verify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!e.text.matches("^[A-Fa-f0-9]*")) {
					e.doit = false;
				}
			}
		});

		text.addListener(SWT.CHANGED, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				dataGroupBits.fromHexString(text.getText());
				sendData();
			}
		});
		return text;
	}

	/**
	 * Create the status decimal text field
	 * 
	 * @return
	 * @category status
	 */
	Text createDecStatusGroupText() {
		Text ret = createText(statusGroup);
		ret.setEditable(false);
		return ret;
	}

	/**
	 * Create the status hex text field
	 * 
	 * @return
	 * @category status
	 */
	Text createHexStatusGroupText() {
		Text ret = createText(statusGroup);
		ret.setEditable(false);
		return ret;
	}

	/**
	 * Create the control decimal text field
	 * 
	 * @return
	 * @category control
	 */
	Text createDecControlGroupText() {
		final Text text = createText(controlGroup);

		text.addListener(SWT.Verify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!e.text.matches("^-?[0-9]*")) {
					e.doit = false;
				}
			}
		});

		text.addListener(SWT.CHANGED, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				controlGroupBits.fromDecString(text.getText());
				sendData();
			}
		});

		return text;
	}

	/**
	 * Create the control decimal text field (masquerade)
	 * 
	 * @return
	 * @category control
	 */
	Text createDecMaskControlGroupText() {
		final Text text = createText(controlGroup);

		text.addListener(SWT.Verify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!e.text.matches("^-?[0-9]*")) {
					e.doit = false;
				}
			}
		});

		text.addListener(SWT.CHANGED, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				controlGroupBits.fromDecStringMascarade(text.getText());
				sendData();
			}
		});

		return text;
	}

	/**
	 * Create the control hex text field
	 * 
	 * @return
	 * @category control
	 */
	Text createHexControlGroupText() {
		final Text text = createText(controlGroup);

		text.addListener(SWT.Verify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!e.text.matches("^[A-Fa-f0-9]*")) {
					e.doit = false;
				}
			}
		});

		text.addListener(SWT.CHANGED, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				controlGroupBits.fromHexString(text.getText());
				sendData();
				// retriveData();
				// refreshGUI();
			}
		});
		return text;
	}

	/**
	 * Create the control hex text field (mascarade)
	 * 
	 * @return
	 * @category control
	 */
	Text createHexMaskControlGroupText() {
		final Text text = createText(controlGroup);

		text.addListener(SWT.Verify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!e.text.matches("^[A-Fa-f0-9]*")) {
					e.doit = false;
				}
			}
		});

		text.addListener(SWT.CHANGED, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				controlGroupBits.fromHexStringMascarade(text.getText());
				sendData();
				// retriveData();
				// refreshGUI();
			}
		});
		return text;
	}

	public GUI(Composite parent, int style) {
		super(parent, style);
		initialize();

	}

	public GUIRefreshThread refreshThread = new GUIRefreshThread();

	public class GUIRefreshThread extends Thread {
		private volatile boolean keepAlive = true;

		@Override
		public void run() {
			while (keepAlive) {
				retriveData();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {

				}
				getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						refreshGUI();
					}
				});
			}
		}

		public void terminate() {
			keepAlive = false;
			try {
				join();
			} catch (InterruptedException e) {

			}
		}
	}

	private void initialize() {
		loadButtonsImages();
		createDataGroup();
		createStatusGroup();
		createControlGroup();
		setLayout(new GridLayout());
		retriveData();
		refreshGUI();
		refreshThread.start();
	}

	/**
	 * This method initializes group
	 * 
	 */
	private void createDataGroup() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		dataGroup = new Group(this, SWT.NONE);
		dataGroup.setText("Base address + 0 (DATA)");
		dataGroup.setLayout(gridLayout);

		for (int i = 7; i >= 0; i--)
			dataGroupButtons[i] = createDataGroupButton(i);

		// Dec label
		GridData decFieldLabelGridData = new GridData();
		decFieldLabelGridData.horizontalSpan = 2;
		Label dataDecFieldLabel = new Label(dataGroup, SWT.NONE);
		dataDecFieldLabel.setText("Dec:");
		dataDecFieldLabel.setLayoutData(decFieldLabelGridData);

		// Fillers
		new Label(dataGroup, SWT.NONE);
		new Label(dataGroup, SWT.NONE);

		// Hex label
		GridData hexFieldLabelGridData = new GridData();
		hexFieldLabelGridData.horizontalSpan = 2;
		Label dataHexFieldLabel = new Label(dataGroup, SWT.NONE);
		dataHexFieldLabel.setText("Hex:");
		dataHexFieldLabel.setLayoutData(hexFieldLabelGridData);

		// Fillers
		new Label(dataGroup, SWT.NONE);
		new Label(dataGroup, SWT.NONE);

		// Dec field
		GridData decFieldTextGridData = new GridData();
		decFieldTextGridData.horizontalSpan = 2;
		decFieldTextGridData.horizontalAlignment = GridData.FILL;
		decFieldTextGridData.grabExcessHorizontalSpace = true;
		dataDecFieldText = createDecDataGroupText();
		dataDecFieldText.setLayoutData(decFieldTextGridData);

		// Fillers
		new Label(dataGroup, SWT.NONE);
		new Label(dataGroup, SWT.NONE);

		// Hex field
		GridData hexFieldTextGridData = new GridData();
		hexFieldTextGridData.horizontalSpan = 2;
		hexFieldTextGridData.horizontalAlignment = GridData.FILL;
		hexFieldTextGridData.grabExcessHorizontalSpace = true;
		dataHexFieldText = createHexDataGroupText();
		dataHexFieldText.setLayoutData(hexFieldTextGridData);
	}

	/**
	 * This method initializes group1
	 * 
	 */
	private void createStatusGroup() {

		// setLayout(new GridLayout());

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		statusGroup = new Group(this, SWT.NONE);
		statusGroup.setText("Base address + 1 (STATUS)");
		statusGroup.setLayout(gridLayout);

		for (int i = 7; i >= 0; i--)
			statusGroupButtons[i] = createStatusGroupButton(i);

		// Fillers
		new Label(statusGroup, SWT.NONE);
		new Label(statusGroup, SWT.NONE);
		new Label(statusGroup, SWT.NONE);
		new Label(statusGroup, SWT.NONE);

		// Explanation text
		GridData explGridData = new GridData();
		explGridData.horizontalSpan = 4;
		explGridData.horizontalAlignment = GridData.END;
		Label expl0 = new Label(statusGroup, SWT.NONE);
		expl0.setText("* d ⊕ 100000000 / 1000");
		expl0.setLayoutData(explGridData);

		// Dec label
		GridData decFieldLabelGridData = new GridData();
		decFieldLabelGridData.horizontalSpan = 2;
		Label statusDecFieldLabel = new Label(statusGroup, SWT.NONE);
		statusDecFieldLabel.setText("Dec:");
		statusDecFieldLabel.setLayoutData(decFieldLabelGridData);

		// Hex label
		GridData hexFieldLabelGridData = new GridData();
		hexFieldLabelGridData.horizontalSpan = 2;
		Label statusHexFieldLabel = new Label(statusGroup, SWT.NONE);
		statusHexFieldLabel.setText("Hex:");
		statusHexFieldLabel.setLayoutData(hexFieldLabelGridData);

		// Masked Dec label
		GridData maskedDecFieldLabelGridData = new GridData();
		maskedDecFieldLabelGridData.horizontalSpan = 2;
		Label statusMaskedDecFieldLabel = new Label(statusGroup, SWT.NONE);
		statusMaskedDecFieldLabel.setText("Dec: *");
		statusMaskedDecFieldLabel.setLayoutData(maskedDecFieldLabelGridData);

		// Masked Hex label
		GridData maskedHexFieldLabelGridData = new GridData();
		maskedHexFieldLabelGridData.horizontalSpan = 2;
		Label statusMaskedHexFieldLabel = new Label(statusGroup, SWT.NONE);
		statusMaskedHexFieldLabel.setText("Hex: *");
		statusMaskedHexFieldLabel.setLayoutData(maskedHexFieldLabelGridData);

		// Dec field
		GridData decFieldTextGridData = new GridData();
		decFieldTextGridData.horizontalSpan = 2;
		decFieldTextGridData.horizontalAlignment = GridData.FILL;
		decFieldTextGridData.grabExcessHorizontalSpace = true;
		statusDecFieldText = createDecStatusGroupText();
		statusDecFieldText.setLayoutData(decFieldTextGridData);

		// Hex field
		GridData hexFieldTextGridData = new GridData();
		hexFieldTextGridData.horizontalSpan = 2;
		hexFieldTextGridData.horizontalAlignment = GridData.FILL;
		hexFieldTextGridData.grabExcessHorizontalSpace = true;
		statusHexFiledText = createHexStatusGroupText();
		statusHexFiledText.setLayoutData(hexFieldTextGridData);

		// Masked Dec field
		GridData maskedDecFieldTextGridData = new GridData();
		maskedDecFieldTextGridData.horizontalSpan = 2;
		maskedDecFieldTextGridData.horizontalAlignment = GridData.FILL;
		maskedDecFieldTextGridData.grabExcessHorizontalSpace = true;
		statusMaskedDecFieldText = createDecStatusGroupText();
		statusMaskedDecFieldText.setLayoutData(maskedDecFieldTextGridData);

		// Masked Hex field
		GridData maskedHexFieldTextGridData = new GridData();
		maskedHexFieldTextGridData.horizontalSpan = 2;
		maskedHexFieldTextGridData.horizontalAlignment = GridData.FILL;
		maskedHexFieldTextGridData.grabExcessHorizontalSpace = true;
		statusMaskedHexFiledText = createHexStatusGroupText();
		statusMaskedHexFiledText.setLayoutData(maskedHexFieldTextGridData);

	}

	/**
	 * This method initializes group11
	 * 
	 */
	private void createControlGroup() {

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		controlGroup = new Group(this, SWT.NONE);
		controlGroup.setText("Base address + 2 (CONTROL)");
		controlGroup.setLayout(gridLayout);

		for (int i = 7; i >= 0; i--)
			controlGroupButtons[i] = createControlGroupButton(i);

		// Fillers
		new Label(controlGroup, SWT.NONE);
		new Label(controlGroup, SWT.NONE);
		new Label(controlGroup, SWT.NONE);
		new Label(controlGroup, SWT.NONE);

		// Explanation text
		GridData explGridData = new GridData();
		explGridData.horizontalSpan = 4;
		Label expl0 = new Label(controlGroup, SWT.NONE);
		explGridData.horizontalAlignment = GridData.END;
		expl0.setText("** (d ⊕ 1011) . 00001111");
		expl0.setLayoutData(explGridData);

		// Dec label
		GridData decFieldLabelGridData = new GridData();
		decFieldLabelGridData.horizontalSpan = 2;
		Label controlDecFieldLabel = new Label(controlGroup, SWT.NONE);
		controlDecFieldLabel.setText("Dec:");
		controlDecFieldLabel.setLayoutData(decFieldLabelGridData);

		// Hex label
		GridData hexFieldLabelGridData = new GridData();
		hexFieldLabelGridData.horizontalSpan = 2;
		Label controlHexFieldLabel = new Label(controlGroup, SWT.NONE);
		controlHexFieldLabel.setText("Hex:");
		controlHexFieldLabel.setLayoutData(hexFieldLabelGridData);

		// Masked Dec label
		GridData maskedDecFieldLabelGridData = new GridData();
		maskedDecFieldLabelGridData.horizontalSpan = 2;
		Label controlMaskedDecFieldLabel = new Label(controlGroup, SWT.NONE);
		controlMaskedDecFieldLabel.setText("Dec: **");
		controlMaskedDecFieldLabel.setLayoutData(maskedDecFieldLabelGridData);

		// Masked Hex label
		GridData maskedHexFieldLabelGridData = new GridData();
		maskedHexFieldLabelGridData.horizontalSpan = 2;
		Label controlMaskedHexFieldLabel = new Label(controlGroup, SWT.NONE);
		controlMaskedHexFieldLabel.setText("Hex: **");
		controlMaskedHexFieldLabel.setLayoutData(maskedHexFieldLabelGridData);

		// Dec field
		GridData decFieldTextGridData = new GridData();
		decFieldTextGridData.horizontalSpan = 2;
		decFieldTextGridData.horizontalAlignment = GridData.FILL;
		decFieldTextGridData.grabExcessHorizontalSpace = true;
		controlDecFieldText = createDecControlGroupText();
		controlDecFieldText.setLayoutData(decFieldTextGridData);

		// Hex field
		GridData hexFieldTextGridData = new GridData();
		hexFieldTextGridData.horizontalSpan = 2;
		hexFieldTextGridData.horizontalAlignment = GridData.FILL;
		hexFieldTextGridData.grabExcessHorizontalSpace = true;
		controlHexFiledText = createHexControlGroupText();
		controlHexFiledText.setLayoutData(hexFieldTextGridData);

		// Masked Dec field
		GridData maskedDecFieldTextGridData = new GridData();
		maskedDecFieldTextGridData.horizontalSpan = 2;
		maskedDecFieldTextGridData.horizontalAlignment = GridData.FILL;
		maskedDecFieldTextGridData.grabExcessHorizontalSpace = true;
		controlMaskedDecFieldText = createDecMaskControlGroupText();
		controlMaskedDecFieldText.setLayoutData(maskedDecFieldTextGridData);

		// Masked Hex field
		GridData maskedHexFieldTextGridData = new GridData();
		maskedHexFieldTextGridData.horizontalSpan = 2;
		maskedHexFieldTextGridData.horizontalAlignment = GridData.FILL;
		maskedHexFieldTextGridData.grabExcessHorizontalSpace = true;
		controlMaskedHexFiledText = createHexMaskControlGroupText();
		controlMaskedHexFiledText.setLayoutData(maskedHexFieldTextGridData);
	}

}

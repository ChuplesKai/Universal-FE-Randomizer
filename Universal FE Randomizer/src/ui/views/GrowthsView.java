package ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import ui.common.GuiUtil;
import ui.general.MinMaxControl;
import ui.model.GrowthOptions;
import ui.model.MinMaxOption;
import ui.model.MinMaxVarOption;


public class GrowthsView extends YuneView<GrowthOptions> {

	private boolean hasSTRMAGSplit;

	private Boolean isEnabled = false;
	private GrowthOptions.Mode currentMode = GrowthOptions.Mode.REDISTRIBUTE;


	private Button enableButton;

	private MinMaxControl growthRangeControl;
	private Spinner varianceSpinner;

	private Button adjustHPGrowths;
	private Button adjustSTRMAGSplit;

	private Combo optionSelect;
	private Label optionDescription;

	/*****************************************************************
	 * 
	 ****************************************************************/
	public GrowthsView(Composite parent, boolean hasSTRMAGSplit) {
		super();
		createGroup(parent);
		this.hasSTRMAGSplit = hasSTRMAGSplit;
		compose();
	}


	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public String getGroupTitle() {
		return "Growths";
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public String getGroupTooltip() {
		return "Randomizes the stat growths of all playable characters.";
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	private void handleChange()
	{
		switch( optionSelect.getText() )
		{
		case "Delta":
			setMode( GrowthOptions.Mode.DELTA );
			break;
		case "Absolute":
			setMode( GrowthOptions.Mode.FULL );
			break;
		case "Redistribute":
			setMode( GrowthOptions.Mode.REDISTRIBUTE );
			break;
		case "Hybrid":
			setMode( GrowthOptions.Mode.HYBRID );
			break;
		default: // Not sure what else to do here, set to full
			setMode( GrowthOptions.Mode.FULL );
		}		
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	protected void compose()
	{
		// First up is the enable button, whether or not we' even doing this.
		enableButton = new Button(group, SWT.CHECK);
		enableButton.setText("Enable Growths Randomization");
		enableButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				setEnableGrowths(enableButton.getSelection());
			}
		});

		// Going to do a drop-down instead to select - more compact, no tooltip
		optionSelect = new Combo(group, SWT.DROP_DOWN);
		optionSelect.add("Delta");
		optionSelect.add("Absolute");
		optionSelect.add("Redistribute");
		optionSelect.add("Hybrid");
		optionSelect.select(0);
		optionSelect.addListener(SWT.Modify, new Listener()
		{
			@Override
			public void handleEvent(Event event) 
			{
				handleChange();
			}
		});
		FormData optionData = new FormData();
		optionData.top = new FormAttachment(enableButton, 5);
		optionData.left = new FormAttachment(enableButton, 5, SWT.LEFT);
		optionData.width = 120;
		optionSelect.setLayoutData( optionData );

		// Then need the variance spinner & Label
		Label redistParamLabel = new Label(group, SWT.RIGHT);
		redistParamLabel.setText("Variance:");

		varianceSpinner = new Spinner(group, SWT.NONE);
		varianceSpinner.setValues(30, 0, 255, 0, 1, 5);
		varianceSpinner.setEnabled(false);

		FormData labelData = new FormData();
		labelData.left = new FormAttachment(0, 5);
		labelData.right = new FormAttachment(varianceSpinner, -5);
		labelData.top = new FormAttachment(varianceSpinner, 0, SWT.CENTER);
		redistParamLabel.setLayoutData(labelData);

		FormData spinnerData = new FormData();
		spinnerData.right = new FormAttachment(100, -5);
		spinnerData.top = new FormAttachment( optionSelect, 0, SWT.TOP );
		varianceSpinner.setLayoutData(spinnerData);


		// Min and Max growth ranges
		growthRangeControl = new MinMaxControl(group, SWT.NONE, "Min Growth:", "Max Growth:");
		growthRangeControl.getMinSpinner().setValues(5, 0, 255, 0, 1, 5);
		growthRangeControl.getMaxSpinner().setValues(80, 0, 255, 0, 1, 5);
		growthRangeControl.setEnabled(false);

		FormData rangeData = new FormData();
		rangeData.top = new FormAttachment(optionSelect, 5);
		rangeData.left = new FormAttachment(0, 5);
		rangeData.right = new FormAttachment(100, -5);
		growthRangeControl.setLayoutData(rangeData);


		// Want a label here instead of a tooltip
		optionDescription = new Label(group, SWT.CENTER | SWT.WRAP);
		optionDescription.setText("Description of the randomization goes here.  If it is extra long, I hope it wraps.");

		labelData = new FormData( 290, 60 );
		labelData.top = new FormAttachment(growthRangeControl, 5);
		optionDescription.setLayoutData(labelData);

		// The button of whether to adjust HP growths
		adjustHPGrowths = new Button(group, SWT.CHECK);
		adjustHPGrowths.setText("Adjust HP Growths");
		adjustHPGrowths.setToolTipText("Whether to include HP growths randomization.  If enabled, grants HP a fixed +25% growth above other stats.");
		adjustHPGrowths.setEnabled(false);

		optionData = new FormData();
		optionData.left = new FormAttachment(optionDescription, 0, SWT.LEFT);
		optionData.top = new FormAttachment(optionDescription, 5);
		adjustHPGrowths.setLayoutData(optionData);

		// If the game has a STR/MAG split, add a check box to help keep those stats randomized appropriately
		if (hasSTRMAGSplit) {
			adjustSTRMAGSplit = new Button(group, SWT.CHECK);
			adjustSTRMAGSplit.setText("Adjust STR/MAG by Class");
			adjustSTRMAGSplit.setToolTipText("Ensures that characters that primarily use magic randomize a higher or equal magic growth than strength and that\ncharacters that primarily use physical attacks randomize a higher or equal strength growth than magic.\n\nCharacters that use both will not be weighted in either direction.");
			adjustSTRMAGSplit.setEnabled(false);

			optionData = new FormData();
			optionData.left = new FormAttachment(adjustHPGrowths, 0, SWT.LEFT);
			optionData.top = new FormAttachment(adjustHPGrowths, 5);
			adjustSTRMAGSplit.setLayoutData(optionData);
		}

		setMode( GrowthOptions.Mode.DELTA ); // Make sure we set this mode as well, but only after everything is allocated
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	public void overrideMaxGrowthAllowed(int maxGrowth) {
		growthRangeControl.getMaxSpinner().setMaximum(maxGrowth);
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	private void setMode(GrowthOptions.Mode newMode)
	{
		currentMode = newMode;
		if (isEnabled)
		{
			varianceSpinner.setEnabled( newMode != GrowthOptions.Mode.FULL && newMode != GrowthOptions.Mode.HYBRID );
			optionDescription.setText( GrowthOptions.getModeDescription(newMode) );
		}
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public GrowthOptions getOptions() 
	{
		if (!isEnabled) { return null; }

		MinMaxVarOption paramOption = new MinMaxVarOption(growthRangeControl.getMinMaxOption(), varianceSpinner.getSelection());
		boolean adjustSTRMAG = adjustSTRMAGSplit != null ? adjustSTRMAGSplit.getSelection() : false;

		return new GrowthOptions(currentMode, paramOption, adjustHPGrowths.getSelection(), adjustSTRMAG);
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	private void setEnableGrowths(Boolean enabled)
	{
		boolean fullmode = (currentMode == GrowthOptions.Mode.FULL || currentMode == GrowthOptions.Mode.HYBRID);
		varianceSpinner.setEnabled(enabled && !fullmode );
		optionSelect.setEnabled(enabled);
		growthRangeControl.setEnabled(enabled);
		adjustHPGrowths.setEnabled(enabled);
		if (adjustSTRMAGSplit != null) { adjustSTRMAGSplit.setEnabled(enabled && currentMode != GrowthOptions.Mode.DELTA); }

		isEnabled = enabled;
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public void initialize(GrowthOptions options)
	{
		if (options == null)
		{
			enableButton.setSelection(false);
			setEnableGrowths(false);
			return;
		}

		enableButton.setSelection(true);
		setEnableGrowths(true);
		// I think I also need to set the text based on the mode here?
		switch (options.mode) 
		{
			case REDISTRIBUTE:
				optionSelect.select(2);
				break;
			case DELTA:
				optionSelect.select(0);
				break;
			case FULL:
				optionSelect.select(1);
				break;
			case HYBRID:
				optionSelect.select(3);
				break;
		}
		setMode( options.mode );

		varianceSpinner.setSelection( options.parameters.variance );
		if (options.parameters.minValue < growthRangeControl.getMinSpinner().getMaximum()) 
		{
			growthRangeControl.setMin(options.parameters.minValue);
			growthRangeControl.setMax(options.parameters.maxValue);
		} 
		else 
		{
			growthRangeControl.setMax(options.parameters.maxValue);
			growthRangeControl.setMin(options.parameters.minValue);
		}

		adjustHPGrowths.setSelection(options.adjustHP);
		if (adjustSTRMAGSplit != null) {
			adjustSTRMAGSplit.setSelection(options.adjustSTRMAGSplit);
		}
	}
}

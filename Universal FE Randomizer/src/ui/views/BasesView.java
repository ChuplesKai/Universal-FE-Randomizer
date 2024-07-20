package ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;

import fedata.general.FEBase.GameType;
import ui.common.GuiUtil;
import ui.model.BaseOptions;
import ui.model.VarOption;

public class BasesView extends YuneView<BaseOptions>
{	
	private Boolean isEnabled = false;
	private BaseOptions.Mode currentMode = BaseOptions.Mode.REDISTRIBUTE;
	

	private Button enableButton;
	
	private Button redistributeOption;
	private Spinner varianceSpinner;
	
	private Button byDeltaOption;
	private Spinner deltaSpinner;

	private Combo optionSelect;
	private Label optionDescription;
	
	private Button adjustSTRMAG;

	/*****************************************************************
	 * 
	 ****************************************************************/
	public BasesView(Composite parent, GameType type)
	{
		super(parent, type);
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public String getGroupTitle() {
		return "Bases";
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public String getGroupTooltip() {
		return "Randomizes the base stat offsets of all playable characters, relative to their class (excluding CON).";
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	private void handleChange()
	{
		switch( optionSelect.getText() )
		{
		case "Delta":
			setMode( BaseOptions.Mode.DELTA );
			break;
		case "Redistribute":
			setMode( BaseOptions.Mode.REDISTRIBUTE );
			break;
		default: // Not sure what else to do here, set to delta
			setMode( BaseOptions.Mode.DELTA );
		}		
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	protected void compose()
	{
		enableButton = new Button(group, SWT.CHECK);
		enableButton.setText("Enable Bases Randomization");
		enableButton.addListener(SWT.Selection, new Listener()
		{
			@Override
			public void handleEvent(Event event)
			{
				setEnableBases(enableButton.getSelection());
			}
		});

		// Going to do a drop-down instead to select - more compact, no tooltip
		optionSelect = new Combo(group, SWT.DROP_DOWN);
		optionSelect.add("Delta");
		optionSelect.add("Redistribute");
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
		varianceSpinner.setValues(4, 0, 10, 0, 1, 1);
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


		// Want a label here instead of a tooltip
		optionDescription = new Label(group, SWT.CENTER | SWT.WRAP);
		optionDescription.setText("Description of the randomization goes here.  If it is extra long, I hope it wraps.");

		labelData = new FormData( 290, 60 );
		labelData.top = new FormAttachment(optionSelect, 5);
		labelData.left = new FormAttachment(optionSelect, 0, SWT.LEFT);
		optionDescription.setLayoutData(labelData);
				
		// Then, in a split STR/MAG game, add checkbox for making randomization more helpful
		if (type.hasSTRMAGSplit())
		{
			adjustSTRMAG = new Button(group, SWT.CHECK);
			adjustSTRMAG.setText("Adjust STR/MAG by Class");
			adjustSTRMAG.setToolTipText("Ensures that characters that primarily use magic randomize a higher or equal magic base than strength and that\ncharacters that primarily use physical attacks randomize a higher or equal strength base than magic.\n\nCharacters that use both will not be weighted in either direction.");
			adjustSTRMAG.setEnabled(false);
			
			optionData = new FormData();
			optionData.left = new FormAttachment(optionDescription, 0, SWT.LEFT);
			optionData.top = new FormAttachment(optionDescription, 10);
			adjustSTRMAG.setLayoutData(optionData);
		}
	}
	
	/*****************************************************************
	 * 
	 ****************************************************************/
	private void setEnableBases(Boolean enabled) 
	{
		optionSelect.setEnabled( enabled );
		varianceSpinner.setEnabled( enabled );
		if (adjustSTRMAG != null) { adjustSTRMAG.setEnabled(enabled); }		
		isEnabled = enabled;
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	private void setMode(BaseOptions.Mode newMode)
	{
		currentMode = newMode;
		if (isEnabled) 
		{
			optionDescription.setText( BaseOptions.getModeDescription(newMode) );			
		}
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public BaseOptions getOptions()
	{
		if (!isEnabled) { return null; }
		
		boolean adjustSTRMAGBases = adjustSTRMAG != null ? adjustSTRMAG.getSelection() : false;
		return new BaseOptions(currentMode, varianceSpinner.getSelection(), adjustSTRMAGBases);
	}

	/*****************************************************************
	 * 
	 ****************************************************************/
	@Override
	public void initialize(BaseOptions options) {
		if (options == null) {
			enableButton.setSelection(false);
			setEnableBases(false);
		}
		else
		{
			enableButton.setSelection(true);
			setEnableBases(true);
			// I think I also need to set the text based on the mode here?
			switch (options.mode) 
			{
				case REDISTRIBUTE:
					optionSelect.select(1);
					break;
				case DELTA:
					optionSelect.select(0);
					break;
			}
			setMode(options.mode);
			varianceSpinner.setSelection( options.variance );

			if (adjustSTRMAG != null)
			{
				adjustSTRMAG.setSelection(options.adjustSTRMAGByClass);
			}
		}
	}

}

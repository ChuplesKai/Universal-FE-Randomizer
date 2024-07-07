package ui.common;

import random.general.FERandom;
import ui.model.DistributionOptions;
import ui.views.YuneView;
import fedata.general.FEBase.GameType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.*;
import util.SeedGenerator;

public class SeedGroup extends YuneView<DistributionOptions> {
    // Random Seed 
    private Text seedField;
    private Button generateButton;
    // Sample Distribution 
    private Button uniformButton;
    private Button triangleButton;
    private Button memoryButton;

    /*****************************************************************
     * 
     ****************************************************************/
    public SeedGroup(Composite parent){
        super(parent);
    }

    /*****************************************************************
     * 
     ****************************************************************/
    @Override
    protected void compose() 
    {
        // Make the Seed Label
        Label seedLabel = new Label(group, SWT.NONE);
        seedLabel.setText("Randomizer Seed Phrase:");
        seedField = new Text(group, SWT.WRAP | SWT.BORDER);

        // Make the Seed generation button
        generateButton = new Button(group, SWT.PUSH);
        generateButton.setText("Generate Phrase");

        // Make the Distribution radio buttons
        uniformButton = new Button( group, SWT.RADIO );
        uniformButton.setText("Uniform");
        uniformButton.setToolTipText("Uniform sampling distribution for ranges and variances - each result is equally likely.");
        uniformButton.setEnabled(true);
        uniformButton.setSelection(true);
        // and triangle
        triangleButton = new Button(group, SWT.RADIO);
        triangleButton.setText("Triangular");
        triangleButton.setToolTipText("Triangular sampling distirbution for ranges and variances - results are more often in the middle.");
        triangleButton.setEnabled(true);
        triangleButton.setSelection(false);

        // Then, checkbox for whether to let the sampler have "memory"
        memoryButton = new Button(group, SWT.CHECK);
        memoryButton.setText("Enable Streak Breaker");
        memoryButton.setToolTipText("When enabled, the sampler keeps track of results, and will try to break streaks of bad or good results.");
        memoryButton.setEnabled(true);
        memoryButton.setSelection(false);

        // The fill-field with the actual random seed
        FormData seedFieldData = new FormData(SWT.DEFAULT, 150); // Left of Label
        seedFieldData.top = new FormAttachment(0, 5);
        seedFieldData.left = new FormAttachment(seedLabel, 5);
        seedFieldData.height = 48;
        seedFieldData.width = 380;
        seedField.setLayoutData(seedFieldData);

        // A label for the fill field
        FormData seedLabelData = new FormData(); // At the same height as the field
        seedLabelData.top = new FormAttachment(seedField, 0, SWT.TOP);
        seedLabelData.left = new FormAttachment(0, 0);
        seedLabel.setLayoutData(seedLabelData);

        // Button to draw another random quote to use as the seed
        FormData generateData = new FormData(); // Below the Label
        generateData.top = new FormAttachment(seedLabel, 5);
        generateData.left = new FormAttachment(seedLabel, 0, SWT.LEFT);
        generateData.width = 150;
        generateButton.setLayoutData(generateData);

        // Set the position for the distribution
        FormData distData = new FormData(); // To the left of the seed field
        distData.top = new FormAttachment(seedField, 3, SWT.TOP);
        distData.left = new FormAttachment(seedField, 5);
        uniformButton.setLayoutData(distData);
        distData = new FormData();
        distData.top = new FormAttachment(uniformButton, 0, SWT.TOP);
        distData.left = new FormAttachment(uniformButton, 5);
        distData.right = new FormAttachment(100, -5); 
        triangleButton.setLayoutData(distData);

        // Finally, set the position for the memory button
        FormData memData = new FormData(); // Below distribution radios
        memData.top = new FormAttachment( uniformButton, 3 );
        memData.left = new FormAttachment( uniformButton, 0, SWT.LEFT );
        memoryButton.setLayoutData(memData);
    }

    /*****************************************************************
     * 
     ****************************************************************/
    public void addGenerateButtonListener(GameType type) {
        // First remove old listeners (previous game)
        for (Listener listener : generateButton.getListeners(SWT.Selection)) {
            generateButton.removeListener(SWT.Selection, listener);
        }

        // Now add a new listener with the current game
        generateButton.addListener(SWT.Selection, selectionEvent -> {
            seedField.setText(SeedGenerator.generateRandomSeed(type));
        });
    }

    /*****************************************************************
     * 
     ****************************************************************/
    public String getSeed() {
        return this.seedField.getText();
    }

    /*****************************************************************
     * 
     ****************************************************************/
    public void setSeedFieldText(String seed) {
        this.seedField.setText(seed);
    }

    /*****************************************************************
     * 
     ****************************************************************/
    @Override
    public String getGroupTitle() {
        return "Random Seed";
    }

    /*****************************************************************
     * initialize - Implementation - sets the values based on options.
     ****************************************************************/
    @Override
    public void initialize(DistributionOptions options) {
        uniformButton.setEnabled(true);
        triangleButton.setEnabled(true);
        memoryButton.setEnabled(true);
        if (options == null)
        {
            uniformButton.setSelection(true);
            triangleButton.setSelection(false);
            memoryButton.setSelection(false);
        } 
        else 
        {
            boolean uniform = options.getDistribution() == FERandom.randDist.UNIFORM ? true : false;
            uniformButton.setSelection( uniform );
            triangleButton.setSelection( !uniform );
            memoryButton.setSelection( options.usingMemory() );
        }
    }

    /*****************************************************************
     * getOptions - Implementation
     ****************************************************************/
    @Override
    public DistributionOptions getOptions() 
    {
        FERandom.randDist distribution = uniformButton.getSelection() ? FERandom.randDist.UNIFORM : FERandom.randDist.TRIANGLE;
        boolean useMemory = memoryButton.getSelection();
        String seed = seedField.getText();

        return new DistributionOptions(distribution, useMemory, seed);
    }


}

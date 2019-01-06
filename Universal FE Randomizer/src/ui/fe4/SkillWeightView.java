package ui.fe4;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ui.fe4.WeightedOptions.Weight;

public class SkillWeightView extends Composite {
	
	private WeightView wrathView;
	private WeightView pursuitView;
	private WeightView adeptView;
	private WeightView charmView;
	private WeightView nihilView;
	private WeightView miracleView;
	private WeightView criticalView;
	private WeightView vantageView;
	private WeightView chargeView;
	private WeightView astraView;
	private WeightView lunaView;
	private WeightView solView;
	private WeightView renewalView;
	private WeightView paragonView;
	private WeightView bargainView;
	
	public SkillWeightView(Composite parent, int style) {
		super(parent, style);
		
		FormLayout formLayout = new FormLayout();
		formLayout.marginTop = 10;
		formLayout.marginLeft = 10;
		setLayout(formLayout);
		
		Label titleLabel = new Label(this, SWT.NONE);
		titleLabel.setText("Skill Weights");
		
		FormData titleData = new FormData();
		titleData.left = new FormAttachment(0, 0);
		titleLabel.setLayoutData(titleData);
		
		Composite header = new Composite(this, SWT.NONE);
		
		FormData headerData = new FormData();
		headerData.left = new FormAttachment(0, 5);
		headerData.top = new FormAttachment(titleLabel, 5);
		headerData.right = new FormAttachment(100, -5);
		header.setLayoutData(headerData);
		
		FormLayout headerLayout = new FormLayout();
		header.setLayout(headerLayout);
		
		Label allowLabel = new Label(header, SWT.NONE);
		allowLabel.setText("Allow?");
		
		FormData allowData = new FormData();
		allowData.left = new FormAttachment(0, 0);
		allowData.top = new FormAttachment(0, 0);
		allowLabel.setLayoutData(allowData);
		
		Label lessLikely = new Label(header, SWT.NONE);
		lessLikely.setText("Less Likely");
		
		FormData lessData = new FormData();
		lessData.left = new FormAttachment(0, 80);
		lessData.top = new FormAttachment(allowLabel, 0, SWT.CENTER);
		lessLikely.setLayoutData(lessData);
		
		Label moreLikely = new Label(header, SWT.NONE);
		moreLikely.setText("More Likely");
		
		FormData moreData = new FormData();
		moreData.right = new FormAttachment(100, -5);
		moreData.top = new FormAttachment(allowLabel, 0, SWT.CENTER);
		moreLikely.setLayoutData(moreData);
		
		wrathView = new WeightView("Wrath", Weight.NORMAL, this, SWT.NONE);
		
		FormData viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(header, 5);
		viewData.right = new FormAttachment(100, -5);
		wrathView.setLayoutData(viewData);
		
		pursuitView = new WeightView("Pursuit", Weight.VERY_HIGH, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(wrathView, 0);
		viewData.right = new FormAttachment(100, -5);
		pursuitView.setLayoutData(viewData);
		
		adeptView = new WeightView("Adept", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(pursuitView, 0);
		viewData.right = new FormAttachment(100, -5);
		adeptView.setLayoutData(viewData);
		
		charmView = new WeightView("Charm", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(adeptView, 0);
		viewData.right = new FormAttachment(100, -5);
		charmView.setLayoutData(viewData);
		
		nihilView = new WeightView("Nihil", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(charmView, 0);
		viewData.right = new FormAttachment(100, -5);
		nihilView.setLayoutData(viewData);
		
		miracleView = new WeightView("Miracle", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(nihilView, 0);
		viewData.right = new FormAttachment(100, -5);
		miracleView.setLayoutData(viewData);
		
		criticalView = new WeightView("Critical", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(miracleView, 0);
		viewData.right = new FormAttachment(100, -5);
		criticalView.setLayoutData(viewData);
		
		vantageView = new WeightView("Wrath", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(criticalView, 0);
		viewData.right = new FormAttachment(100, -5);
		vantageView.setLayoutData(viewData);
		
		chargeView = new WeightView("Charge", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(vantageView, 0);
		viewData.right = new FormAttachment(100, -5);
		chargeView.setLayoutData(viewData);
		
		astraView = new WeightView("Astra", Weight.LOW, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(chargeView, 0);
		viewData.right = new FormAttachment(100, -5);
		astraView.setLayoutData(viewData);
		
		lunaView = new WeightView("Luna", Weight.LOW, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(astraView, 0);
		viewData.right = new FormAttachment(100, -5);
		lunaView.setLayoutData(viewData);
		
		solView = new WeightView("Sol", Weight.LOW, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(lunaView, 0);
		viewData.right = new FormAttachment(100, -5);
		solView.setLayoutData(viewData);
		
		renewalView = new WeightView("Renewal", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(solView, 0);
		viewData.right = new FormAttachment(100, -5);
		renewalView.setLayoutData(viewData);
		
		paragonView = new WeightView("Paragon", Weight.NORMAL, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(renewalView, 0);
		viewData.right = new FormAttachment(100, -5);
		paragonView.setLayoutData(viewData);
		
		bargainView = new WeightView("Bargain", Weight.LOW, this, SWT.NONE);
		
		viewData = new FormData();
		viewData.left = new FormAttachment(header, 0, SWT.LEFT);
		viewData.top = new FormAttachment(paragonView, 0);
		viewData.right = new FormAttachment(100, -5);
		bargainView.setLayoutData(viewData);
	}
	
	public void setEnabled(boolean enabled) {
		wrathView.setEnabled(enabled);
		pursuitView.setEnabled(enabled);
		adeptView.setEnabled(enabled);
		charmView.setEnabled(enabled);
		nihilView.setEnabled(enabled);
		miracleView.setEnabled(enabled);
		criticalView.setEnabled(enabled);
		vantageView.setEnabled(enabled);
		chargeView.setEnabled(enabled);
		astraView.setEnabled(enabled);
		lunaView.setEnabled(enabled);
		solView.setEnabled(enabled);
		renewalView.setEnabled(enabled);
		paragonView.setEnabled(enabled);
		bargainView.setEnabled(enabled);
	}
	
	public SkillWeightOptions getSkillWeights() {
		return new SkillWeightOptions(wrathView.getWeightedOptions(), 
				pursuitView.getWeightedOptions(), 
				adeptView.getWeightedOptions(), 
				charmView.getWeightedOptions(), 
				nihilView.getWeightedOptions(), 
				miracleView.getWeightedOptions(), 
				criticalView.getWeightedOptions(), 
				vantageView.getWeightedOptions(), 
				chargeView.getWeightedOptions(), 
				astraView.getWeightedOptions(), 
				lunaView.getWeightedOptions(), 
				solView.getWeightedOptions(), 
				renewalView.getWeightedOptions(), 
				paragonView.getWeightedOptions(), 
				bargainView.getWeightedOptions());
	}

}

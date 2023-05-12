package ui.gba.tabs;

import fedata.general.FEBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import ui.legacy.ItemAssignmentView;
import ui.legacy.WeaponsView;
import util.OptionRecorder;

public class ItemsTab extends YuneTabItem {

    private WeaponsView weapons;
    private ItemAssignmentView itemAssignment;

    public ItemsTab(CTabFolder parent, FEBase.GameType type) {
        super(parent, type);
    }

    @Override
    protected void compose() {
        GridData weaponsData = new GridData();
        weaponsData.verticalSpan = 3;
        weapons = addView(new WeaponsView(container, SWT.NONE, type), weaponsData);
        itemAssignment = addView(new ItemAssignmentView(container, SWT.NONE, type));
    }

    @Override
    protected String getTabName() {
        return "Items";
    }

    @Override
    protected String getTabTooltip() {
        return "Contains all Setting related to Items. For Example Weapon Stats, and Weapon Assignment";
    }

    @Override
    protected int numberColumns() {
        return 2;
    }

    @Override
    public void preloadOptions(OptionRecorder.GBAOptionBundle bundle) {
        this.weapons.setWeaponOptions(bundle.weapons);
        this.itemAssignment.setItemAssignmentOptions(bundle.itemAssignmentOptions);
    }

    @Override
    public void updateOptionBundle(OptionRecorder.GBAOptionBundle bundle) {
        bundle.weapons = weapons.getWeaponOptions();
        bundle.itemAssignmentOptions = itemAssignment.getAssignmentOptions();
    }
}

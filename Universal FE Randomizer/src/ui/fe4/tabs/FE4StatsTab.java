package ui.fe4.tabs;

import fedata.general.FEBase.GameType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import ui.common.YuneTabItem;
import ui.fe4.FE4EnemyBuffView;
import ui.fe4.HolyBloodView;
import ui.legacy.BasesView;
import ui.legacy.GrowthsView;
import util.OptionRecorder;

public class FE4StatsTab extends YuneTabItem {

    private GrowthsView growths;
    private BasesView bases;
    private HolyBloodView holyBlood;
    private FE4EnemyBuffView enemies;
    public FE4StatsTab(CTabFolder parent) {
        super(parent, GameType.FE4);
    }

    @Override
    protected void compose() {
        growths = addView(new GrowthsView(container, SWT.NONE, type.hasSTRMAGSplit()));
        holyBlood = addView(new HolyBloodView(container, SWT.NONE));
        setViewData(holyBlood, 1, 2);
        enemies = addView(new FE4EnemyBuffView(container, SWT.NONE));
        bases = addView(new BasesView(container, SWT.NONE, type));
    }

    @Override
    protected String getTabName(){
        return "Stats";
    }

    @Override
    protected String getTabTooltip() {
        return "This Tab contains all Setting which are related to the Stats of the Characters. Such as Bases, Growths, Holy Blood.";
    }

    @Override
    protected int numberColumns() {
        return 3;
    }

    @Override
    public void preloadOptions(OptionRecorder.FE4OptionBundle bundle) {
        growths.initialize(bundle.growths);
        bases.initialize(bundle.bases);
        holyBlood.initialize(bundle.holyBlood);
        enemies.initialize(bundle.enemyBuff);
    }

    @Override
    public void updateOptionBundle(OptionRecorder.FE4OptionBundle bundle) {
        bundle.growths = growths.getOptions();
        bundle.bases = bases.getOptions();
        bundle.holyBlood = holyBlood.getOptions();
        bundle.enemyBuff = enemies.getOptions();
    }
}

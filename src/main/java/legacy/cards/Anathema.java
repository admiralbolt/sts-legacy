package legacy.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import legacy.LegacyMod;
import legacy.actions.PermanentBuffAction;
import legacy.characters.TheDefault;

import static legacy.LegacyMod.makeCardPath;

public class Anathema extends CustomCard {

  public static final String ID = LegacyMod.makeID("anathema");
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final String IMG = makeCardPath("Anathema.png");
  public static final String NAME = cardStrings.NAME;
  public static final String DESCRIPTION = cardStrings.DESCRIPTION;

  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ENEMY;
  private static final CardType TYPE = CardType.ATTACK;
  public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

  private static final int COST = 2;
  private static final int DAMAGE_UPGRADE = 2;
  private static final int UPGRADE_PLUS_DMG = 10;

  public Anathema() {
    super(ID, LegacyMod.LEGACY_DB.getName(ID, NAME), IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

    this.baseDamage = LegacyMod.LEGACY_DB.getCardDamage(ID);
    this.baseMagicNumber = this.magicNumber = DAMAGE_UPGRADE;
  }

  // Actions the card should do.
  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.player.decreaseMaxHealth(1);
    AbstractDungeon.actionManager.addToBottom(new PermanentBuffAction(this.cardID, this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
  }

  // Upgraded stats.
  @Override
  public void upgrade() {
    if (!upgraded) {
      upgradeName();
      upgradeDamage(UPGRADE_PLUS_DMG);
      initializeDescription();
    }
  }
}


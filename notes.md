# Notes

Tinker works by modifying the baseBlock / baseDamage of the master card in the deck:

https://github.com/HypoSoc/ClockworkMod/blob/4b2bab27dc0cb53c7def2da1214e94eb20bb63f9/src/main/java/clockworkmod/actions/IncreaseTinkerAction.java



PostCreateStartingDeck is a potential hook for updating base cards on subsequent permanent runs.

https://github.com/daviscook477/BaseMod/blob/1a8a344ecfde07613d29f39ff52b380cf50196f9/mod/src/main/java/basemod/BaseMod.java#L2392

More generally, we could also add additional cards into the deck based on values in the sqlite database. Something very similar could be accomplished for relics using the post create starting relics hook.

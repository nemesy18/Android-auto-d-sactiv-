# Android Auto Toggle (Galaxy S24 + Shizuku)

Mini-app personnelle permettant d'activer/désactiver le package Android Auto `com.google.android.projection.gearhead` via Shizuku, plus une tuile de réglages rapides.

## Utilisation
1. Installer et démarrer Shizuku (débogage sans fil sur Android 11+).
2. Installer cette app, l'ouvrir et toucher **Autoriser Shizuku**.
3. Utiliser les boutons ON/OFF.
4. Samsung : ouvrir le panneau rapide > Modifier > ajouter **Android Auto ON/OFF**.

Le bouton **Ouvrir / démarrer Shizuku** ouvre Shizuku. Pour des raisons Android, l'app ne peut pas démarrer silencieusement Shizuku après un redémarrage.

## Compilation
Ouvrir le dossier dans Android Studio et lancer `assembleDebug`, ou pousser le projet sur GitHub et lancer le workflow Actions fourni. L'APK debug se trouve dans `app/build/outputs/apk/debug/`.

## Attention
Désactiver Android Auto peut interrompre une session en cours. Sur certaines versions Samsung/Android, les permissions ADB/Shizuku peuvent différer. Testé conceptuellement pour Android récent ; vérifier sur l'appareil avant de compter dessus en conduite.

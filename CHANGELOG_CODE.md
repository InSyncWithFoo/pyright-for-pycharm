<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Code changelog


This page documents code changes.
For user-facing changes, see [`CHANGELOG.md`][_-1].


  [_-1]: ./CHANGELOG.md


## [Unreleased]

### Added

* "Copy Pyright command" action is added. (HEAD)

### Changed

* [`PyrightBundle`][60-1] and [`PyrightIcon`][60-2] are renamed. (7032faff)

### Fixed

* [`PyrightExternalAnnotator`][60-3] now overrides
  `collectInformation(PsiFile, Editor, boolean)`
  instead of `collectInformation(PsiFile)`. (7032faff)


  [60-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/7032faff/src/main/kotlin/com/insyncwithfoo/pyright/Bundle.kt
  [60-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/7032faff/src/main/kotlin/com/insyncwithfoo/pyright/Icon.kt
  [60-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/7032faff/src/main/kotlin/com/insyncwithfoo/pyright/PyrightExternalAnnotator.kt


## [0.5.0] - 2024-06-04

### Added

* [The documentation][50-1] is updated.
  * [`logging.md`][50-2] is added.
  
  (34b7c498, 7d02d73b, 8238bfd4)

* Global option "Process timeout" is added. (f3b7585f)

### Changed

* [`PyrightRunner` and related classes][50-3] are rewritten. (f3b7585f)

### Fixed

* [`SuppressQuickFix`][50-4] names are now stored as messages. (e2d9ca2b)


  [50-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/8238bfd4/docs
  [50-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/7d02d73b/docs/logging.md
  [50-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/f3b7585f/src/main/kotlin/com/insyncwithfoo/pyright/runner
  [50-4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/e2d9ca2b/src/main/kotlin/com/insyncwithfoo/pyright/annotations/SuppressQuickFix.kt


## [0.4.0] - 2024-05-27

### Added

* The plugin now supports 2024.2. (18897c60)
* Global option "Minimum severity level" is added. (74267a2c)
* [Documentation for LSP-specific settings][40-1] is updated.
  (9dbfbb86, b3ffed47, 69406c38, ebea0b86)

### Changed

* [The Kover Gradle Plugin][40-2] is updated to 0.8.0. (9d583cd2)
* [The Qodana Gradle plugin][40-3] and its corresponding action
  [@JetBrains/qodana-action][40-4] are updated to 2024.1.5.
  (7d2278a5)
* [Kotlin JVM plugin][40-5] is updated to 2.0.0. (99770f0e)
* [`HintIcon.toString()`][40-6] now returns an empty string.
  This is due to `ExpUiIcons` being deprecated. (96465451)


  [40-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/ebea0b86/docs/configurations/lsp.md
  [40-2]: https://github.com/Kotlin/kotlinx-kover
  [40-3]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [40-4]: https://github.com/JetBrains/qodana-action
  [40-5]: https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
  [40-6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/96465451/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PathResolvingHint.kt


## [0.3.1] - 2024-05-16

### Fixed

* `HighlightSeverity` members' names may contain spaces,
  which causes [`ProblemHighlightType.valueOf()`][31-1] to throw.
  A workaround is introduced by manually replacing `" "` with `"_"`. (cdd51d86)


  [31-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/cdd51d86/src/main/kotlin/com/insyncwithfoo/pyright/PyrightExternalAnnotator.kt


## [0.3.0] - 2024-05-13

### Added

* [Documentation for LSP-specific settings][30-1] is added.
  (d8fd3b82, 115fabf6, 519c54d1, 5608c8da, bc55d40f, 31982c84)
* [Message key names][30-2] are slightly changed. (dc1240d4)
* `AnnotationApplier` and newly added related constructs for quick fixes
  are moved to the [`.annotations`][30-3] module.
  Corresponding tests and documentation are also added.
  (2c9b87fe, ae653d79, 8c98a033)

### Changed

* [The Qodana Gradle plugin][30-4] and its corresponding action
  [@JetBrains/qodana-action][30-5] are updated to 2024.1.4.
  (ba0e2aca, e9eafd54, f4331b4c, 0f89d093, a58e9621, 913fe613)
* A new branch is added to [`executablePathResolvingHint()`][30-6]. (fb98eae7)
* [Kotlin JVM plugin][30-7] is updated to 1.9.24. (908f95a8)
* `RoamingType.LOCAL` is used for [application-level configurations][30-8]
  instead of `RoamingType.DISABLED`. (15540255)
* The "Run Plugin" task now runs with the new UI enabled and
  the `.idea` subdirectory not hidden. (38b8c86a)


  [30-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/31982c84/docs/configurations/lsp.md
  [30-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/dc1240d4/src/main/resources/messages/pyright.properties
  [30-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/8c98a033/src/main/kotlin/com/insyncwithfoo/pyright/annotations
  [30-4]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [30-5]: https://github.com/JetBrains/qodana-action
  [30-6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/fb98eae7/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PathResolvingHint.kt
  [30-7]: https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
  [30-8]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/15540255/src/main/kotlin/com/insyncwithfoo/pyright/configuration/application/ConfigurationService.kt


## [0.2.0] - 2024-04-17

### Added

* Three new properties are added to [`PyrightInspection`][20-1]
  to allow configuring highlight severity levels.
  [Corresponding documentation][20-2] is added.
  (0d6188f9, 25917886)
* [Documentation for LSP-specific settings][20-3] is added.
  (25917886, cc71fb23)

### Changed

* [@gradle/actions/wrapper-validation][20-4] is updated to 3.3.0.
  (a8f6d5d6, 5cff663e)


  [20-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/25917886/src/main/kotlin/com/insyncwithfoo/pyright/PyrightInspection.kt
  [20-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/25917886/docs/configurations/common.md
  [20-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/cc71fb23/docs/configurations/lsp.md
  [20-4]: https://github.com/gradle/actions/wrapper-validation


## [0.1.0] - 2024-04-10

### Added

* [`LICENSE_TEMPLATE.txt`][10-1] is added. (e4b257e5)

### Changed

* `released.yaml` is renamed to [`publish.yml`][10-2].
  Steps overlapping with other jobs are removed. (aff01a5e)
* Default highlight severity levels are now the same as sister's. (5fccec40)
* The "Always use global" and "Auto-suggest executable" are moved back
  to the first column of the first row. (6e6c6df6)
* [@gradle/wrapper-validation-action][10-3] is updated to 2.1.3. (1c38c5ac)
* [The docs][10-4] are rewritten to reflect both plugins. (4d1f29b6, 5e997e73)

### Fixed

* [`PyrightConfigurable`][10-5]'s `apply()`, `isModified()` and `reset()`
  now call the corresponding methods of `panel`.
  This fixes a(nother) regression introduced in v0.1.0-mvp.5
  which has been causing the configuration panels to be unresetable. (2eb24204)


  [10-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/e4b257e5/LICENSE_TEMPLATE.txt
  [10-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/aff01a5e/.github/workflows/publish.yaml
  [10-3]: https://github.com/gradle/wrapper-validation-action
  [10-4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/5e997e73/docs
  [10-5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/2eb24204/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PyrightConfigurable.kt


## [0.1.0-mvp.6] - 2024-04-07

### Changed

* [`mkdocs-material`][6-1] is updated to 9.5.17. (371c02c1)
* [`pluginIcon.svg`][6-2] is resized to 40 by 40 to comply with
  [the Approval Guidelines][6-3]. (caf56f68)
* [`README.md`][6-4] is rewritten to alter the plugin description. (fd655b50)
* Bug reports and feature requests now have automatic assignees. (0f8ead3a)
* [`build.yaml`][6-5] now runs tests on all three platforms. (c309ad98)

### Fixed

* A `panel.apply()` call is added to
  [`PyrightConfigurable.isModified()`][6-6].
  This ensures that the state of panel is synchronized
  before being compared with the original state,
  which was not the case in v0.1.0-mvp.5. (6ac49d29)
* The test module `configuration2` is renamed to [`configuration`][6-7].
  (8f558f9d)


  [6-1]: https://github.com/squidfunk/mkdocs-material
  [6-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/caf56f68/src/main/resources/META-INF/pluginIcon.svg
  [6-3]: https://plugins.jetbrains.com/legal/approval-guidelines
  [6-4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/fd655b50/README.md
  [6-5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/c309ad98/.github/workflows/build.yaml
  [6-6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/6ac49d29/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PyrightConfigurable.kt
  [6-7]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/8f558f9d/src/test/kotlin/com/insyncwithfoo/pyright/configuration


## [0.1.0-mvp.5] - 2024-03-31

### Added

* [`PathResolvingHint.kt`][5-1], [corresponding testcases][5-2]
  and test resources are added. (6e4f8c7c)

### Changed

* The [`build.yaml`][5-3] workflow now:
  * Edits old releases when the changelogs are changed, and
  * Uploads corresponding artifacts as new drafts are created.
  
  The two helper Python scripts are added under [`.scripts`][5-4].
  (e9500f03)

* [The Qodana Gradle plugin][5-5] and its corresponding action
  [@JetBrains/qodana-action][5-6] are updated to 2023.3.2. (caca9092)
* UI-related code is rewritten to use [Kotlin UI DSL][5-7]. (6e4f8c7c)
* All APIs are now either internal or private. (6e4f8c7c)
  * Some of them no longer have the prefix `Pyright` in their names. (6e4f8c7c)
* [The IntelliJ Platform Gradle plugin][5-8] is updated to 1.17.3. (26fae6a3)

### Changed

* The `configuration.common` module is removed
  in favor of [`PyrightConfigurable.kt`][5-9]. (6e4f8c7c)


  [5-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/6e4f8c7c/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PathResolvingHint.kt
  [5-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/6e4f8c7c/src/test/kotlin/com/insyncwithfoo/pyright/configuration/PathResolvingHintTest.kt
  [5-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/e9500f03/.github/workflows/build.yaml
  [5-4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/e9500f03/.scripts/
  [5-5]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [5-6]: https://github.com/JetBrains/qodana-action
  [5-7]: https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html
  [5-8]: https://github.com/JetBrains/intellij-platform-gradle-plugin
  [5-9]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/6e4f8c7c/src/main/kotlin/com/insyncwithfoo/pyright/configuration/PyrightConfigurable.kt


## [0.1.0-mvp.4] - 2024-03-24

### Added

* [`faq.md`][4-1], [`features.md`][4-2] and [`problems.md`][4-3] are added.
  (99cb7753, 233aca23, 4e3d921a, b1c782bf, 6a0cd5ac, 624ce941, dbe24020)
* [`FUNDING.yaml`][4-4] is added. (6a536243)
* A list of frequently asked questions is added to the docs.
  (99cb7753, 233aca23, 4e3d921a, b1c782bf)
* Global option "Prefix tooltips" is added. (a3f9ca46)
* Project option "Auto-suggest executable" is added.
  (6a0cd5ac, 624ce941, ff9b6d92)
* Configuration field tests are added. (28f88c04)
* The inspection display name is changed. (64d8e273)

### Changed

* Node 16 to 20 transition:
  * The `develop` branch of [@s0/git-publish-subdir-action][4-5]
    is used instead of `v2.6.0`. (a8613613)
* The project/repository is renamed from
  `pyright-plugin` to `pyright-for-pycharm`. (d9253998)
* [The Gradle Qodana plugin][4-6] is updated to 2023.3.1. (aa32c1b9)
* Configuration constructs are now marked `internal`. (54d74f74)
* [`CHANGELOG.md`][4-7] is rewritten. (c02db458, 4650c88c, 7516e63d)
* Notifications-related logic is rewritten. (6b4b8e2a, 6a0cd5ac)
* Duplicate SVG files are now converted to symlinks. (e2ed56fa)
  The targets are those in the [`resources/icons`][4-8] directory. (e2ed56fa)
* Messages are merged into one single bundle. (aeed108d)
* The "Always use global" option is moved
  to the second column of the second row. (624ce941)
* [@gradle/wrapper-validation-action][4-9] is updated to 2.1.2. (c9c27dda)
* Gradle is updated to 8.7. (ce835b88)
* `PyrightExternalAnnotator.kt`/`String.toPreformatted()`
  is refactored to use `HtmlChunk`. (6be9fd81)

### Fixed

* Documentation pages now have working edit links. (3365ac71, 5faaf33f)
* Workflows are rewritten and reformatted. (c485479e, 1285a3d8, 5e8e1ee3)
* `invokeLater()` is now only called if an executable is given. (cc7a4c66)


  [4-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/dbe24020/docs/faq.md
  [4-2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/dbe24020/docs/features.md
  [4-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/dbe24020/docs/problems.md
  [4-4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/6a536243/.github/FUNDING.yaml
  [4-5]: https://github.com/s0/git-publish-subdir-action/tree/develop
  [4-6]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [4-7]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/7516e63d/CHANGELOG.md
  [4-8]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/e2ed56fa/src/main/resources/icons
  [4-9]: https://github.com/gradle/wrapper-validation-action


## [0.1.0-mvp.3] - 2024-03-10

### Changed

* [`release.yaml`][3-1] no longer runs on prereleases. (ed9ce98d)
* SVG files are reformatted. (c9512f44)
* [`foojay-resolver-convention`][3-2] is updated to 0.8.0. (1f0d9a04)
* Kotlin is updated to 1.9.23. (6a0e537b)
* The `configuration` module is refactored. (9c1d5a05, a328920d)
* Error-reporting logic is added. (61fbb0bc)

### Fixed

* [`build.yaml`][3-3] now works correctly.
  Distributions can now be downloaded from [the `build` branch][3-4],
  [the <i>Actions</i> tab][3-5], or [the <i>Releases</i> tab][3-6].
  (6349cddf, a2c6fb83)


  [3-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/ed9ce98d/.github/workflows/release.yaml
  [3-2]: https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
  [3-3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/a2c6fb83/.github/workflows/build.yaml
  [3-4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/tree/build
  [3-5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml
  [3-6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/releases


## [0.1.0-mvp.2] - 2024-03-03

### Added

* [`CODE_OF_CONDUCT.md`][2-1] is added. (048ad24e)
* Paths to executables are now resolved against the project path. (18fed10f)
* [User documentation][2-2] is added. (d4b5aa9f, 2d926821)
* Global option "Use editor font" is added. (43d253d1)

### Changed

* Error-handling logic for `saveDocumentAsIs()` is removed. (930e06f7)
* `PyrightException` is now `sealed`. (40c43683)
* Add "Build" run configuration. (a073dccb)
* Annotation-applying logic is extracted to a dedicated class. (83587afe)
* Commands are now logged as JSON. (b3426911)

### Removed

* Support for 2023.3.3 is discontinued. (9c8981d4)

### Fixed

* The bug report template is rewritten.
  Previously it was a blind copy-and-paste from
  [@JetBrains/intellij-platform-plugin-template][2-3]. (4efd384b)
* The project interpreter path is now also passed to the executable,
  allowing the corresponding packages to be recognized correctly. (83587afe)


  [2-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/048ad24e/CODE_OF_CONDUCT.md
  [2-2]: https://insyncwithfoo.github.io/pyright-for-pycharm/
  [2-3]: https://github.com/JetBrains/intellij-platform-plugin-template


## [0.1.0-mvp.1] - 2024-02-26

### Added

* Project initialized.


  [Unreleased]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.5.0..HEAD
  [0.5.0]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.4.0..v0.5.0
  [0.4.0]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.3.1..v0.4.0
  [0.3.1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.3.0..v0.3.1
  [0.3.0]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.2.0..v0.3.0
  [0.2.0]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0..v0.2.0
  [0.1.0]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.6..v0.1.0
  [0.1.0-mvp.6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.5..v0.1.0-mvp.6
  [0.1.0-mvp.5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.4..v0.1.0-mvp.5
  [0.1.0-mvp.4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.3..v0.1.0-mvp.4
  [0.1.0-mvp.3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.2..v0.1.0-mvp.3
  [0.1.0-mvp.2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.1..v0.1.0-mvp.2
  [0.1.0-mvp.1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/commits

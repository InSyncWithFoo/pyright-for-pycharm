<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog


## [Unreleased]

### Added

* [`FUNDING.yaml`][4-1] is added. (6a536243)
* A list of frequently asked questions is added to the docs.
  (99cb7753, 233aca23, 4e3d921a, b1c782bf)
* Global option "Prefix tooltips" is added. (a3f9ca46)
* Project option "Auto-suggest executable" is added. (6a0cd5ac, HEAD)
* Configuration field tests are added. (28f88c04)

### Changed

* Node 16 to 20 transition:
  * The `develop` branch of [@s0/git-publish-subdir-action][4-2]
    is used instead of `v2.6.0`. (a8613613)
  * Commit `4eb285e` of [@peaceiris/actions-gh-pages][4-3]
    is used instead of `v3`. (HEAD)
* The project/repository is renamed from
  `pyright-plugin` to `pyright-for-pycharm`. (d9253998)
* [The Gradle Qodana plugin][4-4] is updated to 2023.3.1. (aa32c1b9)
* Configuration constructs are now marked `internal`. (54d74f74)
* [`CHANGELOG.md`][4-5] is rewritten. (c02db458, 4650c88c, HEAD)
* Notifications-related logic is rewritten. (6b4b8e2a, 6a0cd5ac)
* Duplicate SVG files are now converted to symlinks.
  The targets are those in the [`resources/icons`][4-6] directory. (e2ed56fa)
* Messages are merged into one single bundle. (aeed108d)

### Fixed

* Documentation pages now have working edit links. (3365ac71, 5faaf33f)
* Workflows are rewritten and reformatted. (c485479e, 1285a3d8, 5e8e1ee3)
* `invokeLater()` is now only called if an executable is given. (cc7a4c66)


  [4-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/.github/FUNDING.yaml
  [4-2]: https://github.com/s0/git-publish-subdir-action/tree/develop
  [4-3]: https://github.com/peaceiris/actions-gh-pages
  [4-4]: https://plugins.gradle.org/plugin/org.jetbrains.qodana
  [4-5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/CHANGELOG.md
  [4-6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/src/main/resources/icons


## [0.1.0-mvp.3] - 2024-03-10

### Changed

* `release.yaml` no longer runs on prereleases. (ed9ce98d)
* SVG files are reformatted. (c9512f44)
* [`foojay-resolver-convention`][3-1] is updated to 0.8.0. (1f0d9a04)
* Kotlin is updated to 1.9.23. (6a0e537b)
* The `configuration` module is refactored. (9c1d5a05, a328920d)
* Error-reporting logic is added. (61fbb0bc)

### Fixed

* `build.yaml` now works correctly. (6349cddf, a2c6fb83)


  [3-1]: https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention


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
* Support for 2023.3.3 is discontinued. (9c8981d4)

### Fixed

* The bug report template is rewritten.
  Previously it was a blind copy-and-paste from
  [@JetBrains/intellij-platform-plugin-template][2-3]. (4efd384b)
* The project interpreter path is now also passed to the executable,
  allowing the corresponding packages to be recognized correctly. (83587afe)


  [2-1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/blob/master/CODE_OF_CONDUCT.md
  [2-2]: https://insyncwithfoo.github.io/pyright-for-pycharm/
  [2-3]: https://github.com/JetBrains/intellij-platform-plugin-template


## [0.1.0-mvp.1] - 2024-02-26

### Added

* Project initialized.


  [Unreleased]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.3..HEAD
  [0.1.0-mvp.3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.2..v0.1.0-mvp.3
  [0.1.0-mvp.2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.1..v0.1.0-mvp.2
  [0.1.0-mvp.1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/commits

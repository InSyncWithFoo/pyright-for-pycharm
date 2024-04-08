<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

This page documents user-facing changes.
For code changes, see [`CHANGELOG_CODE.md`][_-1].


  [_-1]: ./CHANGELOG_CODE.md


## [Unreleased]

<i>This section is currently empty.</i>


## [0.1.0-mvp.6] - 2024-04-07

### Fixed

* v0.1.0-mvp.5 has a bug causing the configurations to not be saved.
  This has been fixed.


## [0.1.0-mvp.5] - 2024-03-31

### Added

* UI hints are added to ease the process of setting paths.


## [0.1.0-mvp.4] - 2024-03-24

### Added

* Tooltips can now be prefixed with "Pyright:".
* The plugin will now suggest setting an executable
  for the current project if one can be found locally.
  To turn off suggestion for a project, uncheck
  the corresponding option in the project configuration panel.

### Changed

* Saves will no longer be performed if:
  * The corresponding inspection is disabled, or
  * The executable is not given.
* The notification group name is now "*Pyright notifications*"
  instead of "*Pyright*".
* UI components and messages are slightly changed.
* The inspection name is changed from "Pyright inspection"
  to "Pyright inspection<b>s</b>".

### Removed

* Error notification titles no longer have the "Pyright:" prefix.


## [0.1.0-mvp.3] - 2024-03-10

### Changed

* Errors are now reported as notifications.


## [0.1.0-mvp.2] - 2024-03-03

### Added

* Relative paths to executables are now resolved against the project path.
* Tooltips can now be displayed in editor font.

### Removed

* 2023.3.3 is no longer supported.

### Fixed

* The interpreter of the current project is now correctly recognized.


## [0.1.0-mvp.1] - 2024-02-26

### Added

* Project initialized.


  [Unreleased]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.6..HEAD
  [0.1.0-mvp.6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.5..v0.1.0-mvp.6
  [0.1.0-mvp.5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.4..v0.1.0-mvp.5
  [0.1.0-mvp.4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.3..v0.1.0-mvp.4
  [0.1.0-mvp.3]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.2..v0.1.0-mvp.3
  [0.1.0-mvp.2]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.1.0-mvp.1..v0.1.0-mvp.2
  [0.1.0-mvp.1]: https://github.com/InSyncWithFoo/pyright-for-pycharm/commits

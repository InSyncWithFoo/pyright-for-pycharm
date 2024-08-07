<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

This page documents user-facing changes.
For code changes, see [`CHANGELOG_CODE.md`][_-1].


  [_-1]: ./CHANGELOG_CODE.md


## [Unreleased]

### Added

* The icon for `pyrightconfig.json` is now replaced with Pyright's logo.


## [0.6.0] - 2024-07-21

### Added

* The command for the currently selected file can now be copied
  using the newly added "Copy Pyright command" action.
  This is useful for debugging purposes.
* Experimental LSP support is now available via the LSP4IJ plugin.
* The executable can now be configured to run on multiple threads
  via an UI option.

### Fixed

* The annotator now always run, regardless of other annotators' results.
  Previously, it would not run if previous analyses resulted in errors.
* The active interpreter is now detected correcly on non-PyCharm IDEs.


## [0.5.0] - 2024-06-04

### Changed

* Errors are now detected and handled in a more user-friendly way,
  with notifications and proper actions.
  Previously, they would only be logged in the IDE's log file.


## [0.4.0] - 2024-05-27

### Added

* Diagnostics can now be limited by severity levels.

### Changed

* UI components are slightly changed.


## [0.3.1] - 2024-05-16

### Fixed

* v0.3.0 has a bug causing an internal error
  if the inspections' levels are set as "Weak warnings".
  This has been fixed.


## [0.3.0] - 2024-05-13

### Added

* Diagnostics can now be suppressed using quick fixes.

### Changed

* An informational hint will now be given if
  the given executable file's name is not a known name.
* Application-level settings can now be exported using the
  <i>Export Settings...</i> action.
  Previously they would be omitted when exporting
  and can only be found in the IDE's configuration directory.


## [0.2.0] - 2024-04-17

### Added

* Highlight severity levels are now configurable
  using the options provided in the <i>Inspection</i> panel.
  The inspection is renamed to "Pyright diagnostics".


## [0.1.0] - 2024-04-10

### Changed

* Default highlight severity levels are now
  "Error", "Warning" and "Weak warning" instead of
  "Warning", "Weak warning" and "Weak warning".
  A future release will allow configuring these levels.
* v0.1.0-mvp.5 has a bug causing the configurations to be always unresetable.
  This has been fixed.
* Configuration panels are slightly changed.


## [0.1.0-mvp.6] - 2024-04-07

### Fixed

* v0.1.0-mvp.5 has a bug causing the configurations to be always unsavable.
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
* Configuration panels UI and messages are slightly changed.
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


  [Unreleased]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.6.0..HEAD
  [0.6.0]: https://github.com/InSyncWithFoo/pyright-for-pycharm/compare/v0.5.0..v0.6.0
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

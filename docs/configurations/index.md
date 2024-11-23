The plugin provides two configuration panels,
one IDE-level and one project-level.
They can be found under the <i>Tools</i> section of the <i>Settings</i> panel.

Project-level configurations only override the IDE-level counterparts
when the corresponding "Override" checkboxes are selected.

Project-level settings are stored in the `pyright.xml` file
under the `.idea` directory and can be shared with other people 
(for example, via version-control systems)
to serve as the default settings for the project.

Override settings are stored in the `pyright-overrides.xml` file
in the same directory. This file should not be committed,
as its purpose is to allow overriding project defaults.

!!! note

    To configure highlight severity levels,
    use [the inspection entry][3]'s corresponding pane.


## Minimum requirements

For the plugin to work, at least one [executable][1] needs to be specified.
Note that different [running modes][2] require different executables.


  [1]: executables.md
  [2]: running-modes.md
  [3]: inspection.md

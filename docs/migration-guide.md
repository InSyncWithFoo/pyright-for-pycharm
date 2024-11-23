# How to migrate from <i>Pyright Language Server</i>

Provided that you never modify the settings files manually,
follow these three steps, in this <em>exact</em> order:

* Uninstall <i>Pyright Language Server</i>
* Install <i>Pyright</i>
* Restart IDE (<strong>important</strong>)

Existing settings will be migrated automatically.
Restarting the IDE is necessary for this to work correctly.


## What if I modified the settings file?

Look for the `pyright-langserver.xml` under
the `.idea` subdirectory of your project.
All old settings are stored in that file.

The same applies for version 0.6.0 of the <i>Pyright</i> plugin:
its settings are stored in `pyright.xml` along with 0.7.0's settings.

# Pyright for PyCharm

[![Build](https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml/badge.svg)](https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml)
[![Docs](https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/docs.yaml/badge.svg)](https://insyncwithfoo.github.io/pyright-for-pycharm)
[![Version](https://img.shields.io/jetbrains/plugin/v/24145)][4]
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/24145)][4]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24145)][4]

![](docs/assets/cli-demo1.png)

<!-- Plugin description -->
Pyright integration for PyCharm.

This plugin runs [the Pyright type checker][1] on-the-fly
as you edit your Python files.

It works by saving all your files as-is before running
the executable provided by you. If you are not OK with that,
please <em>do not</em> install this plugin.

Note: If you use PyCharm Professional,
install [the <i>Pyright Language Server</i> plugin][2] instead.


## Usage

Go to <b>Settings</b> | <b>Tools</b> |
<b>Pyright (Global)</b> / <b>Pyright (Project)</b>
and set the path to your Pyright executable.

Save, return to your file and start making some modifications.
You should see the annotations in a few seconds.

See [the docs][3] for more information on the configurations and features.


  [1]: https://github.com/microsoft/pyright
  [2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm
  [3]: https://insyncwithfoo.github.io/pyright-for-pycharm/
<!-- Plugin description end -->


## Installation

This plugin [is available][4] on the Marketplace.
You can also download the ZIP files manually from [the <i>Releases</i> tab][5],
[the `build` branch][6] or [the <i>Actions</i> tab][7]
and follow the instructions described [here][8].

Currently supported versions:
2024.1 (build 241.14494.241) - 2024.1.* (build 241.*).


## Credits

Most of the code is derived from [@koxudaxi/ruff-pycharm-plugin][9].
It is such a fortune that that plugin does almost the same thing
and is also written in Kotlin, and hence easily understandable.

The SVG and PNG logos are derived from [the README image][10]
of the [@microsoft/pyright][1] repository,
generated using Inkscape's autotrace feature.

Some other files are based on or copied directly from
[@JetBrains/intellij-platform-plugin-template][11].


  [4]: https://plugins.jetbrains.com/plugin/24145
  [5]: https://github.com/InSyncWithFoo/pyright-for-pycharm/releases
  [6]: https://github.com/InSyncWithFoo/pyright-for-pycharm/tree/build
  [7]: https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml
  [8]: https://www.jetbrains.com/help/pycharm/managing-plugins.html#install_plugin_from_disk
  [9]: ./CONTRIBUTING.md
  [9]: https://github.com/koxudaxi/ruff-pycharm-plugin
  [10]: https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png
  [11]: https://github.com/JetBrains/intellij-platform-plugin-template

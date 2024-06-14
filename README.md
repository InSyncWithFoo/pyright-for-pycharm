# Pyright for PyCharm

[![Build](https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml/badge.svg)][4]
[![Docs](https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/docs.yaml/badge.svg)][5]
[![Version](https://img.shields.io/jetbrains/plugin/v/24145)][6]
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/24145)][7]
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24145)][8]

![](docs/assets/cli-demo-2.png)

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

(Not sure what this means? See [the docs][3] for more information.)

Save, return to your file and start making some modifications.
You should see the annotations in a few seconds.


  [1]: https://github.com/microsoft/pyright
  [2]: https://github.com/InSyncWithFoo/pyright-langserver-for-pycharm
  [3]: https://insyncwithfoo.github.io/pyright-for-pycharm/configurations/common/#executable
<!-- Plugin description end -->


## Installation

This plugin [is available][8] on the Marketplace.
You can also download the ZIP files manually from [the <i>Releases</i> tab][9],
[the `build` branch][10] or [the <i>Actions</i> tab][11]
and follow the instructions described [here][12].

Currently supported versions:
2024.1 (build 241.14494.241) - 2024.2.* (build 242.*).


## Credits

Most of the code is derived from [@koxudaxi/ruff-pycharm-plugin][13].
It is such a fortune that that plugin does almost the same thing
and is also written in Kotlin, and hence easily understandable.

The SVG and PNG logos are derived from [the README image][14]
of the [@microsoft/pyright][1] repository,
generated using Inkscape's autotrace feature.

Some other files are based on or copied directly from
[@JetBrains/intellij-platform-plugin-template][15].


  [4]: https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml
  [5]: https://insyncwithfoo.github.io/pyright-for-pycharm
  [6]: https://plugins.jetbrains.com/plugin/24145/versions
  [7]: https://plugins.jetbrains.com/plugin/24145/reviews
  [8]: https://plugins.jetbrains.com/plugin/24145
  [9]: https://github.com/InSyncWithFoo/pyright-for-pycharm/releases
  [10]: https://github.com/InSyncWithFoo/pyright-for-pycharm/tree/build
  [11]: https://github.com/InSyncWithFoo/pyright-for-pycharm/actions/workflows/build.yaml
  [12]: https://www.jetbrains.com/help/pycharm/managing-plugins.html#install_plugin_from_disk
  [13]: https://github.com/koxudaxi/ruff-pycharm-plugin
  [14]: https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png
  [15]: https://github.com/JetBrains/intellij-platform-plugin-template

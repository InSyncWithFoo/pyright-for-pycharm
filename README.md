# Pyright for PyCharm

<!-- Plugin description -->
Pyright integration for PyCharm.

This plugin runs [the Pyright type checker][1] on-the-fly
as you edit your Python files.

It works by saving all your files as-is before running
the executable provided by you. If you are not OK with that,
please <em>do not</em> install this plugin.


  [1]: https://github.com/microsoft/pyright
<!-- Plugin description end -->


## Installation

This plugin has not been published to [the Marketplace][2].
Download the ZIP file manually from <i>Releases</i>
and follow the instructions described [here][3].


## Usage

![](https://raw.githubusercontent.com/InSyncWithFoo/pyright-plugin/master/.github/readme/demo1.png)

Go to <b>Settings</b> | <b>Tools</b> |
<b>Pyright (Global)</b> / <b>Pyright(Project)</b> and
set the executable path to your Pyright executable.

Save, return to your file and start making some modifications.
You should see Pyright annotations in a few seconds.


## Known issues

This is only a minimum viable product.

* Tests have not been written,
  and manual tests have only been performed on Windows.
* There are no documentations; the UX might be terrible for some.
* Since `ExternalAnnotator`s are only called after all other annotators
  have run, expect a major delay (up to a few seconds) between checks.

If you are interested in contributing to this project,
see [the contributing guide][4].


## Credits

Most of the code is derived from [@koxudaxi/ruff-pycharm-plugin][5].
It is such a fortune that that plugin does almost the same thing
and is also written in Kotlin, and hence easily understandable.

The SVG logo is derived from [the README image][6]
of the [@microsoft/pyright][1] repository,
generated using Inkscape's autotrace feature.


## See also

* [Pyright (Experimental)][7], its non-working sister plugin
  for PyCharm Professional, using the experimental LSP API.


  [2]: https://plugins.jetbrains.com/
  [3]: https://www.jetbrains.com/help/pycharm/managing-plugins.html#install_plugin_from_disk
  [4]: ./CONTRIBUTING.md
  [5]: https://github.com/koxudaxi/ruff-pycharm-plugin
  [6]: https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png
  [7]: https://github.com/InSyncWithFoo/pyright-pycharm-plugin

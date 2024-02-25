# Pyright for PyCharm

<!-- Plugin description -->
Pyright integration for PyCharm.

This plugin runs [the Pyright type checker][1] on-the-fly
as you edit your Python files.

It works by saving all your files as-is before running
the executable provided by you. If you are not OK with that,
<em>do not</em> install this plugin.


  [1]: https://github.com/microsoft/pyright
<!-- Plugin description end -->


## Installation

This plugin has not been published to [the Marketplace][2].


## Known issues

This is only a minimum viable product.

* Tests have not been written,
  and manual tests have only been performed on Windows.
* There are no documentations; the UX might be terrible for some.
* Since `ExternalAnnotator` implementations are only called after all
  other annotators have run, expect a major delay between checks.

Use at your own risk.


## Credits

Most of the code is derived from [@koxudaxi/ruff-pycharm-plugin][3].
It is such a fortune that that plugin does almost the same thing
and is also written in Kotlin, and hence easily understandable.

The SVG logo is derived from [the README image][4]
of the [@microsoft/pyright][1] repository,
generated using Inkscape's autotrace feature.


## See also

* [Pyright (Experimental)][5], its non-working sister plugin
  for PyCharm Professional, using the experimental LSP API.


  [2]: https://plugins.jetbrains.com/
  [3]: https://github.com/koxudaxi/ruff-pycharm-plugin
  [4]: https://github.com/microsoft/pyright/blob/main/docs/img/PyrightLarge.png
  [5]: https://github.com/InSyncWithFoo/pyright-pycharm-plugin

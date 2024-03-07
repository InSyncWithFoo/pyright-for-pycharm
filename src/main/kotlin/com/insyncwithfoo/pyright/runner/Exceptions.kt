package com.insyncwithfoo.pyright.runner


internal sealed class PyrightException(message: String) : Exception(message)


internal class FatalException(message: String) : PyrightException(message)


internal class InvalidConfigurationsException(message: String) : PyrightException(message)


internal class InvalidParametersException(message: String) : PyrightException(message)

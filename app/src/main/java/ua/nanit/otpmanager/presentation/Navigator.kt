package ua.nanit.otpmanager.presentation

interface Navigator {

    fun navToManualAdd()

    fun navToScanCode()

    fun navToExport()

    fun navToExportQr()

    fun navToImport()

    fun navToImportQr()

    fun navToSettings()

    fun navToAbout()

    fun navUp()

    fun navUpToMain()

}
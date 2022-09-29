package ua.nanit.otpmanager.presentation

interface Navigator {

    fun navToManualAdd()

    fun navToScanCode()

    fun navToExport()

    fun navToExportFile()

    fun navToExportQr()

    fun navToImport()

    fun navToImportFile()

    fun navToImportQr()

    fun navToAbout()

    fun navUp()

    fun navUpToMain()

}
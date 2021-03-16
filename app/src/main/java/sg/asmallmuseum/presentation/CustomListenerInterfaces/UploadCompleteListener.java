package sg.asmallmuseum.presentation.CustomListenerInterfaces;

public interface UploadCompleteListener {
    void onUploadComplete(boolean status, String path, int result_code);
}

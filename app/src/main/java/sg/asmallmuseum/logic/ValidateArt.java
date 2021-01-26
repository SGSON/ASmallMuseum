package sg.asmallmuseum.logic;

import android.net.Uri;

import java.util.List;

import sg.asmallmuseum.Domain.Messages.ArtAttachedError;
import sg.asmallmuseum.Domain.Messages.ArtDescError;
import sg.asmallmuseum.Domain.Messages.ArtTitleError;
import sg.asmallmuseum.Domain.Messages.ArtGenreError;
import sg.asmallmuseum.Domain.Messages.ArtTypeError;
import sg.asmallmuseum.Domain.Messages.CustomException;

public class ValidateArt {
    private static final String TYPE_ERROR = "Please select the type.";
    private static final String GENRE_ERROR = "Please select the genre.";
    private static final String TITLE_ERROR = "Please enter the title of the art.";
    private static final String DESC_ERROR = "Please enter the description of the art.";
    private static final String ATTACHED_ERROR = "Please attached the files.";

    public static void validateAll(List<Uri> paths, List<String> ext, String type, String genre, String title, String author, String date, String desc) throws CustomException {
        validateType(type);
        validateGenre(genre);
        validateTitle(title);
        validateDesc(desc);
        validateAttached(paths);
    }

    public static void validateType(String type) throws ArtTypeError {
        if (type.equals("Select")){
            throw new ArtTypeError(TYPE_ERROR);
        }
    }

    public static void validateGenre(String genre) throws ArtGenreError{
        if (genre.equals("Select")){
            throw new ArtGenreError(GENRE_ERROR);
        }
    }

    public static void validateTitle(String title) throws ArtTitleError{
        if (title.equals("")){
            throw new ArtTitleError(TITLE_ERROR);
        }
    }

    public static void validateDesc(String desc) throws ArtDescError {
        if (desc.equals("")){
            throw new ArtDescError(DESC_ERROR);
        }
    }

    public static void validateAttached(List<Uri> paths) throws ArtAttachedError {
        if (paths.size() == 0){
            throw new ArtAttachedError(ATTACHED_ERROR);
        }
    }
}

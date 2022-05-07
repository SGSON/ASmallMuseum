package xyz.asmallmuseum.android.logic;

import android.net.Uri;

import java.util.List;

import xyz.asmallmuseum.android.Domain.Messages.ArtAttachedError;
import xyz.asmallmuseum.android.Domain.Messages.ArtDescError;
import xyz.asmallmuseum.android.Domain.Messages.ArtTitleError;
import xyz.asmallmuseum.android.Domain.Messages.ArtTypeError;
import xyz.asmallmuseum.android.Domain.Messages.ArtCategoryError;
import xyz.asmallmuseum.android.Domain.Messages.CustomException;

public class ValidateArt {
    private static final String CATEGORY_ERROR = "Please select a category.";
    private static final String TYPE_ERROR = "Please select a type.";
    private static final String TITLE_ERROR = "Please enter the title of the art.";
    private static final String DESC_ERROR = "Please enter the description of the art.";
    private static final String ATTACHED_ERROR = "Please attached the files.";

    public static void validateAll(List<Uri> paths, List<String> ext, String category, String type, String title, String author, String date, String desc) throws CustomException {
        validateTitle(title);
        validateCategory(category);
        validateType(type);
        validateDesc(desc);
        validateAttached(paths);
    }

    public static void validateCategory(String category) throws ArtCategoryError {
        if (category == null || category.equals("Select")){
            throw new ArtCategoryError(CATEGORY_ERROR);
        }
    }

    public static void validateType(String type) throws ArtTypeError {
        if (type == null || type.equals("Select")){
            throw new ArtTypeError(TYPE_ERROR);
        }
    }

    public static void validateTitle(String title) throws ArtTitleError{
        if (title == null || title.equals("")){
            throw new ArtTitleError(TITLE_ERROR);
        }
    }

    public static void validateDesc(String desc) throws ArtDescError {
        if (desc == null || desc.equals("")){
            throw new ArtDescError(DESC_ERROR);
        }
    }

    public static void validateAttached(List<Uri> paths) throws ArtAttachedError {
        if (paths == null || paths.size() == 0){
            throw new ArtAttachedError(ATTACHED_ERROR);
        }
    }
}

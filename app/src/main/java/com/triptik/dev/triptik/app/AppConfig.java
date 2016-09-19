package com.triptik.dev.triptik.app;

public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/register.php";

    //Server Upload Triptik images and add entry into database
    public static String URL_NEW_TRIPTIK = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/triptikSave.php";

    //Server Get comments for the open Triptik
    public static String URL_GET_COMMENTS = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/getCommentsForTriptik.php";

    //Server Get comments for the open Triptik
    public static String URL_GET_COMMENT_COUNT = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/getCommentsCountForTriptik.php";

    //Server Upload Comments
    public static String URL_POST_COMMENTS = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/upload_comment.php";

    //Server Update a Comments visibility
    public static String URL_UPDATE_COMMENT_VISIBILITY = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/update_comment_visibility.php";

    //Server Update a Comments Text
    public static String URL_UPDATE_COMMENT_TEXT = "http://www.fluidmotion.ie/TEST_LAB/triptik_PHP/update_comment_text.php";
}
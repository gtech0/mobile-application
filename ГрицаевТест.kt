fun isCorrect(NAME: String): Boolean
{
    if (NAME[0].isDigit()) return false;
    return NAME.matches(Regex("[a-zA-Z0-9_$]+"));
}

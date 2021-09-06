package com.andyadc.codeblocks.captcha.word;

import java.util.concurrent.ThreadLocalRandom;

public class RandomWordFactory implements WordFactory {

    protected String characters;
    protected int minLength;
    protected int maxLength;

    public RandomWordFactory() {
        characters = "absdegkmnopwx23456789";
        minLength = 6;
        maxLength = 6;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getNextWord() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        int l = minLength + (maxLength > minLength ? rnd.nextInt(maxLength - minLength) : 0);
        for (int i = 0; i < l; i++) {
            int j = rnd.nextInt(characters.length());
            sb.append(characters.charAt(j));
        }
        return sb.toString();
    }
}

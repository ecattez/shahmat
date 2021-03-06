package dev.ecattez.shahmat.domain.game;

public enum GameType {

    CLASSICAL {
        @Override
        <T> T accept(GameTypeVisitor<T> visitor) {
            return visitor.visitClassical();
        }
    };

    abstract <T> T accept(GameTypeVisitor<T> visitor);

}

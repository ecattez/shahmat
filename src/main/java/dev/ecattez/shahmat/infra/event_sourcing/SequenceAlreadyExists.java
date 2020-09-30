package dev.ecattez.shahmat.infra.store;

public class SequenceAlreadyExists extends RuntimeException {

    public SequenceAlreadyExists(Long sequence) {
        super("Sequence " + sequence + " already exists");
    }

}

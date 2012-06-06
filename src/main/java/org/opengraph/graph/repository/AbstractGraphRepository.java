package org.opengraph.graph.repository;

public abstract class AbstractGraphRepository implements GraphRepository {

    private String directory;

    protected abstract String getDirectory();

    protected abstract String getFileName();

    protected String getBaseDirectory() {
        return directory;
    }

    @Override
    public void setBaseDirectory(String directory) {
        this.directory = directory;
    }


}

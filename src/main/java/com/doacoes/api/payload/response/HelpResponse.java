package com.doacoes.api.payload.response;

public class HelpResponse {
    private Long id;
    private String title;
    private String description;
    private String cep;
    private String city;
    private String uf;
    private String urlImage;

    public HelpResponse(Long id, String title, String description, String cep, String city, String uf, String urlImage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cep = cep;
        this.city = city;
        this.uf = uf;
        this.urlImage = urlImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
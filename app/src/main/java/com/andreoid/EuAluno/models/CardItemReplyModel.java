package com.andreoid.EuAluno.models;

public class CardItemReplyModel {
    public String idReply;

    public String author;
    public String reply_content;
    public String data_reply;
    public CardItemReplyModel(String idReply, String author, String reply_content, String data_reply) {
        this.idReply = idReply;
        this.author = author;
        this.reply_content = reply_content;
        this.data_reply = data_reply;

    }
}
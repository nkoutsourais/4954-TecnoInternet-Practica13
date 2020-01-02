package es.codeurjc.daw.mockmvc;

import es.codeurjc.daw.*;

class PostBuilder {

    public Post getPostWithoutComment() {
        String titulo = "titulo";
        String content = "descripcion";
        return new Post(titulo, content);
    }

    public Comment getComment() {
        return new Comment("Neo", "mensaje");
    }

    public Post getPostWithOneComment() {
        Post post = getPostWithoutComment();
        Comment comment = getComment();
        comment.setId(1);
        post.addComment(comment);
        return post;
    }
}
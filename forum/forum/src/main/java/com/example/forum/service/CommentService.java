package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    /*
     * レコード全件取得処理
     */
    public List<CommentForm> findAllComment() {
        List<Comment> results = commentRepository.findAllByOrderByIdDesc();
        List<CommentForm> comments = setCommentForm(results);
        return comments;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setComment(result.getComment());
            comment.setContentId(result.getContentId());
            comment.setCreatedDate(result.getCreatedDate());
            comments.add(comment);
        }
        return comments;
    }

    /*
     * レコード追加
     */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setComment(reqComment.getComment());
        comment.setContentId(reqComment.getContentId());
        comment.setUpdatedDate(LocalDateTime.now());//現在日時を設定する
        return comment;
    }

    /*
     * 編集するコメントを１件取得
     */
    public CommentForm editComment(Integer id) {
        List<Comment> results = new ArrayList<>();
        results.add((Comment) commentRepository.findById(id).orElse(null)); //nullかもしれない（optional）
        List<CommentForm> comments = setCommentForm(results);
        return comments.get(0);
    }

    /*
     *投稿の削除
     */
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}

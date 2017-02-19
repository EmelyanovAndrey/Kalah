package com.pesikovlike.kalah.model.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Igor on 31.10.2016.
 */
@Entity
@Table(name = "avatar", schema = "kalah", catalog = "kalah")
public class Avatar {
    private long avatarId;
    private String avatarName;
    private String filePath;
    private Set<User> usersByAvatarId = new LinkedHashSet<User>();

    public Avatar(){}

    @Id
    @Column(name = "avatar_id", nullable = false)
    public long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(long avatarId) {
        this.avatarId = avatarId;
    }

    @Basic
    @Column(name = "avatar_name", nullable = false, length = -1)
    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    @Basic
    @Column(name = "file_path", nullable = false, length = -1)
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Avatar that = (Avatar) o;

        if (avatarId != that.avatarId) return false;
        if (avatarName != null ? !avatarName.equals(that.avatarName) : that.avatarName != null) return false;
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (avatarId ^ (avatarId >>> 32));
        result = 31 * result + (avatarName != null ? avatarName.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        return result;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "avatar")
    public Set<User> getUsersByAvatarId() {
        return usersByAvatarId;
    }

    public void setUsersByAvatarId(Set<User> usersByAvatarId) {
        this.usersByAvatarId = usersByAvatarId;
    }
}

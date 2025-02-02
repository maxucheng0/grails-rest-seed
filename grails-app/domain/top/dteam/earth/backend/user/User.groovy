package top.dteam.earth.backend.user

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.OffsetDateTime

@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    String displayName
    boolean enabled = true
    boolean passwordExpired = false
    boolean accountExpired = false
    boolean accountLocked = false
    OffsetDateTime dateCreated

    static transients = ['accountExpired', 'accountLocked']

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    boolean hasRole(String role) {
        authorities*.authority.contains(role)
    }

    boolean hasAnyRole(List<String> role) {
        !authorities*.authority.intersect(role as Set<String>).empty
    }

    static constraints = {
        username nullable: false, blank: false, maxSize: 11, unique: true
        password nullable: false, blank: false, password: true, maxSize: 64
        displayName nullable: false, blank: false, maxSize: 30, unique: true
    }

    static mapping = {
        comment '用户'
        table 'myuser'
        dynamicUpdate true
        username comment: '用户名（手机）', index: 'idx_user_username'
        password column: 'user_password', comment: '密码'
        displayName comment: '昵称'
        enabled comment: '是否有效'
        passwordExpired comment: '密码过期'
        dateCreated comment: '创建时间', sqlType: 'timestamptz', index: 'idx_date_created'
    }

}

package org.example.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "store")
public class StoreEntity extends BaseEntity {

    @Column(name = "store_name", nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "active", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private boolean active = true;

    @OneToMany(mappedBy = "storeEntity")
    private Set<StoreBookEntity> storeBooks = new HashSet<>();
}

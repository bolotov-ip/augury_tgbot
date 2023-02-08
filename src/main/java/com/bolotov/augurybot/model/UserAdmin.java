package com.bolotov.augurybot.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("admin")
public class UserAdmin extends User{
}

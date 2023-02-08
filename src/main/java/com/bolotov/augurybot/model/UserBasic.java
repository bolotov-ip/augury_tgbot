package com.bolotov.augurybot.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("user")
public class UserBasic extends User{
}

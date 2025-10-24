package org.example.personalblog.mapper;

import org.example.personalblog.dto.SubscriptionRequest;
import org.example.personalblog.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionRequest subscriptionRequest);
}

import React, {memo} from 'react';
import {Dimensions, StyleSheet} from 'react-native';
import {Avatar, Surface, Text} from 'react-native-paper';
import type {Contact} from 'react-native-contacts';

const {width} = Dimensions.get('window');
const imageSize = width / 2 - 20;

const LargeCardItem = ({contact}: {contact: Contact}) => {
  return (
    <Surface style={styles.container}>
      {contact.hasThumbnail ? (
        <Avatar.Image size={imageSize} source={{uri: contact.thumbnailPath}} />
      ) : (
        <Avatar.Text
          size={imageSize}
          label={contact.displayName?.[0].toUpperCase()}
        />
      )}
      <Text variant="bodyLarge" numberOfLines={1}>
        {contact.displayName}
      </Text>
      <Text variant="bodyMedium">{contact.phoneNumbers?.[0]?.number}</Text>
    </Surface>
  );
};

export default memo(LargeCardItem);

const styles = StyleSheet.create({
  container: {
    flexDirection: 'column',
    alignItems: 'center',
    paddingHorizontal: 12,
    paddingVertical: 8,
    marginHorizontal: 4,
    marginVertical: 2,
    width: '100%',
  },
});

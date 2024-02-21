import React, {memo} from 'react';
import {StyleSheet, TouchableOpacity} from 'react-native';
import {Avatar, Card, IconButton} from 'react-native-paper';
import type {Contact} from 'react-native-contacts';

const ListItem = ({
  contact,
  onImagePress,
}: {
  contact: Contact;
  onImagePress?: () => void;
}) => {
  return (
    <Card style={styles.container}>
      <Card.Title
        title={contact.displayName}
        subtitle={contact.phoneNumbers?.[0]?.number}
        left={(props: any) => (
          <TouchableOpacity onPress={onImagePress}>
            {contact.hasThumbnail ? (
              <Avatar.Image {...props} source={{uri: contact.thumbnailPath}} />
            ) : (
              <Avatar.Icon {...props} icon={'account'} />
            )}
          </TouchableOpacity>
        )}
        right={(props) => (
          <IconButton {...props} icon="phone" onPress={() => {}} />
        )}
      />
    </Card>
  );
};

export default memo(ListItem);

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: 12,
    paddingVertical: 8,
    margin: 2,
  },
});

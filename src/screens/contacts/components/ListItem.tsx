import React, {memo} from 'react';
import {StyleSheet, TouchableOpacity, View} from 'react-native';
import {Avatar, Card, IconButton, MD3Colors} from 'react-native-paper';
import type {Contact} from 'react-native-contacts';
import stringToColor from 'string-to-color';
import Ionicons from 'react-native-vector-icons/Ionicons';

const ListItem = ({
  contact,
  onPress,
  onIconPress,
  onCallPress,
}: {
  contact: Contact;
  onPress?: () => void;
  onIconPress: () => void;
  onCallPress: () => void;
}) => {
  return (
    <Card style={styles.container} onPress={onPress}>
      <Card.Title
        title={contact.displayName}
        subtitle={contact.phoneNumbers?.[0]?.number}
        left={(props: any) =>
          contact.hasThumbnail ? (
            <Avatar.Image {...props} source={{uri: contact.thumbnailPath}} />
          ) : (
            <Avatar.Text
              {...props}
              label={contact.displayName?.[0].toUpperCase()}
              style={{backgroundColor: stringToColor(contact.displayName)}}
            />
          )
        }
        right={(props) => (
          <View style={styles.row}>
            <IconButton {...props} icon="phone" onPress={onCallPress} />
            <TouchableOpacity activeOpacity={0.8} onPress={onIconPress}>
              <Ionicons
                color={MD3Colors.neutralVariant0}
                name={'information-circle-outline'}
                size={22}
              />
            </TouchableOpacity>
          </View>
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
  row: {
    flexDirection: 'row',
    alignItems: 'center',
  },
});

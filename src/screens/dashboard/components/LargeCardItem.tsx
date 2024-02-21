import React, {memo} from 'react';
import {Dimensions, StyleSheet, TouchableOpacity, View} from 'react-native';
import {Avatar, Button, Surface, Text} from 'react-native-paper';
import type {Contact} from 'react-native-contacts';
import Ionicons from 'react-native-vector-icons/Ionicons';

const {width} = Dimensions.get('window');
const imageSize = width / 2 - 30;

const LargeCardItem = ({
  contact,
  onPress,
  onIconPress,
}: {
  contact: Contact;
  onPress: () => void;
  onIconPress: () => void;
}) => {
  return (
    <Surface style={styles.container}>
      <TouchableOpacity
        activeOpacity={0.6}
        style={styles.innerContainer}
        onPress={onPress}>
        {contact.hasThumbnail ? (
          <Avatar.Image
            size={imageSize}
            source={{uri: contact.thumbnailPath}}
          />
        ) : (
          <Avatar.Text
            size={imageSize}
            label={contact.displayName?.[0].toUpperCase()}
          />
        )}
        <View style={styles.row}>
          <View style={styles.detail}>
            <Text variant="bodyLarge" numberOfLines={1}>
              {contact.displayName}
            </Text>
            <Text variant="bodyMedium" numberOfLines={1}>
              {contact.phoneNumbers?.[0]?.number}
            </Text>
          </View>
          <Button onPress={onIconPress}>
            <Ionicons name={'information-circle-outline'} size={22} />
          </Button>
        </View>
      </TouchableOpacity>
    </Surface>
  );
};

export default memo(LargeCardItem);

const styles = StyleSheet.create({
  container: {
    marginHorizontal: 2,
    marginVertical: 2,
    width: '98%',
  },
  innerContainer: {
    flexDirection: 'column',
    alignItems: 'center',

    paddingHorizontal: 12,
    paddingVertical: 8,
    width: '100%',
  },

  row: {
    alignItems: 'center',
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  detail: {
    flex: 1,
  },
});

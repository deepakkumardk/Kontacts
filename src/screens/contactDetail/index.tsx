import React, {useLayoutEffect} from 'react';
import {StyleSheet, View} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import Contacts from 'react-native-contacts';
import {Avatar, IconButton, Text} from 'react-native-paper';
import {AppScreenProps} from 'src/navigation';

const imageSize = 120;

export const ContactDetail = ({
  navigation,
  route,
}: AppScreenProps<'ContactDetail'>) => {
  const {contact} = route.params;
  console.log('ContactDetail -> contact', contact);

  useLayoutEffect(() => {
    navigation.setOptions({
      headerTitle: '',
      headerRight: () => (
        <View style={styles.row}>
          <IconButton
            icon={'account-edit'}
            onPress={() => {
              Contacts.openExistingContact(contact);
            }}
          />
        </View>
      ),
    });
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.center}>
        {contact.hasThumbnail ? (
          <Avatar.Image
            source={{uri: contact.thumbnailPath}}
            size={imageSize}
          />
        ) : (
          <Avatar.Icon icon={'account'} size={imageSize} />
        )}
        <Text variant="headlineSmall">{contact.displayName}</Text>
      </View>

      <Text variant="bodyLarge">{'Phone Number'}</Text>
      {contact.phoneNumbers.map((phone) => (
        <Text key={phone.label}>
          {phone.label} : {phone.number}
        </Text>
      ))}
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    marginHorizontal: 12,
    flex: 1,
  },
  row: {
    flexDirection: 'row',
    flex: 1,
  },
  center: {
    alignItems: 'center',
    padding: 8,
  },
});

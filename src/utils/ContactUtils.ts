import {Linking} from 'react-native';
import Contacts, {type Contact} from 'react-native-contacts';

const sortContacts = (contacts: Contact[]) => {
  return contacts
    .filter((item: Contact) => item.displayName && item.phoneNumbers.length)
    .sort((a, b) => {
      const aName = a.displayName;
      const bName = b.displayName;
      if (aName < bName) {
        return -1;
      }
      if (aName > bName) {
        return 1;
      }
      return 0;
    });
};

export const ContactUtils = {
  getAll: async () => {
    try {
      const contacts = await Contacts.getAll();
      return sortContacts(contacts);
    } catch (error) {
      console.warn('getAll: -> error', error);
      return [];
    }
  },
  getSearchedContacts: async (query: string) => {
    try {
      let contacts: Contact[] = [];
      const isNumber = !isNaN(parseFloat(query)) && isFinite(query as any);
      if (isNumber) {
        contacts = await Contacts.getContactsByPhoneNumber(query);
      } else {
        contacts = await Contacts.getContactsMatchingString(query);
      }
      return sortContacts(contacts);
    } catch (error) {
      console.warn('getSearchedContacts= -> error', error);
      return [];
    }
  },

  call: async (contact: Contact) => {
    try {
      await Linking.openURL('tel:' + contact.phoneNumbers[0]?.number);
    } catch (error) {
      console.log('call -> error', error);
    }
  },
};

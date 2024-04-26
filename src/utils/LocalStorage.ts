import {MMKV} from 'react-native-mmkv';

export const storage = new MMKV();

export const LocalStorage = {
  get: (key: string) => {
    try {
      const value = storage.getString(key);
      return !value ? null : JSON.parse(value);
    } catch (error) {
      console.log('LocalStorage -> get -> error', error);
      return null;
    }
  },
  set: (key: string, value: any) => {
    try {
      storage.set(key, JSON.stringify(value));
    } catch (error) {
      console.log('LocalStorage -> set -> error', error);
    }
  },
  delete: (key: string) => {
    try {
      storage.delete(key);
    } catch (error) {
      console.log('LocalStorage -> delete -> error', error);
    }
  },
  clear: () => {
    try {
      storage.clearAll();
    } catch (error) {
      console.log('LocalStorage -> clear -> error', error);
    }
  },
};

import {Platform} from 'react-native';
import {
  checkMultiple,
  requestMultiple,
  PERMISSIONS,
} from 'react-native-permissions';

export const PermissionUtils = {
  requestContactsPermission: async () => {
    const androidPermissions = [
      PERMISSIONS.ANDROID.READ_CONTACTS,
      PERMISSIONS.ANDROID.WRITE_CONTACTS,
      PERMISSIONS.ANDROID.READ_CALL_LOG,
    ];
    const iOSPermissions = [PERMISSIONS.IOS.CONTACTS];
    const permissions =
      Platform.OS === 'android' ? androidPermissions : iOSPermissions;

    try {
      const hasPermissions = await checkMultiple(permissions);
      console.log(
        'requestContactsPermission: -> hasPermissions',
        hasPermissions,
      );

      const isAllGranted = (response: typeof hasPermissions) =>
        Object.values(response).every((value: any) => value === 'granted');

      if (isAllGranted(hasPermissions)) {
        return true;
      }
      const response = await requestMultiple(permissions);
      return isAllGranted(response);
    } catch (error) {
      console.log('requestContactsPermission: -> error', error);
      return false;
    }
  },
};

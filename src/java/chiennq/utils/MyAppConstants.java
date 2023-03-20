/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.utils;

/**
 *
 * @author Ezarp
 */
public class MyAppConstants {
    private MyAppConstants() {
    }
    
    public class LoginFeatures {
        private LoginFeatures() {
        }
        
        public static final String AUTO_LOGIN_ACTION = "autoLoginAction";
        public static final String INVALID_PAGE = "invalidPage";
        public static final String LOGIN_PAGE = "loginPage";
        public static final String LOGIN_PAGE_RESULT = "loginPageResult";
        
        public static final String LOGIN_ACTION = "loginAction";
        public static final String SEARCH_ACTION = "searchAction";
    }
    
    public class AutoLoginFeatures {
        private AutoLoginFeatures() {
        }
        
        public static final String AUTO_LOGIN_ACTION = "autoLoginAction";
    }
    
    public class RegisterFeatures {
        private RegisterFeatures() {
        }
        
        public static final String LOGIN_PAGE = "loginPage";
        public static final String REGISTER_PAGE = "registerPage";
        public static final String REGISTER_RESULT_PAGE = "registerResultPage";
    }
    
    public class LogoutFeatures {
        private LogoutFeatures() {
        }
        
        public static final String LOGIN_PAGE = "loginPage";
    }
    
    public class SearchAccountFeatures {
        private SearchAccountFeatures() {
        }
        
        
        public static final int DEFAULT_PAGE_NUMBER = 1;
        public static final int DEFAULT_SIZE_NUMBER = 5;
        public static final String SEARCH_AS_ADMIN_PAGE = "searchAsAdminPage";
        public static final String SEARCH_AS_USER_PAGE = "searchAsUserPage";
    }
    
    public class UpdateAccountFeatures {
        private UpdateAccountFeatures() {
        }
        
        public static final String SEARCH_ACTION = "searchAction";
    }
    
    public class DeleteAccountFeatures {
        private DeleteAccountFeatures() {
        }
        
        public static final String SEARCH_ACTION = "searchAction";
    }
    
    public class ShoppingFeatures {
        private ShoppingFeatures() {
        }
        
        public static final int DEFAULT_PAGE_NUMBER = 1;
        public static final int DEFAULT_SIZE_NUMBER = 10;
        public static final String SHOPPING_PAGE = "shoppingPage";
    }
    
    public class AddToCartFeatures {
        private AddToCartFeatures() {
        }
        
        public static final String SHOPPING_PAGE = "shoppingPage";
        public static final String SHOPPING_VIEW = "shoppingView";
    }
    
    public class RemoveFromCartFeatures {
        private RemoveFromCartFeatures() {
        }
        
        public static final String VIEW_CART = "cartPage";
    }
    
    public class CheckoutFeatures {
        private CheckoutFeatures() {
        }
        
        public static final String VIEW_CART = "cartPage";
        public static final String RECEIPT_ACTION = "receiptAction";
        public static final String RECEIPT_PAGE = "receiptPage";
    }
}
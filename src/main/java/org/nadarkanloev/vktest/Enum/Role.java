package org.nadarkanloev.vktest.Enum;


/**
 * Перечисление `Role` определяет возможные роли пользователей в системе.

 * Роли используются для управления доступом к различным функциям и ресурсам системы.
 */
public enum Role {

    /**
     * Роль администратора. Администраторы имеют полный доступ ко всем функциям системы.
     */
    ROLE_ADMIN,

    /**
     * Роль для просмотра постов. Пользователи с этой ролью могут только просматривать посты, но не могут их создавать или редактировать.
     */
    ROLE_POSTS_VIEWER,

    /**
     * Роль для редактирования постов. Пользователи с этой ролью могут просматривать и редактировать посты.
     */
    ROLE_POSTS_EDITOR,

    /**
     * Стандартная пользовательская роль. Пользователи с этой ролью имеют ограниченный набор функций.
     */
    ROLE_USER,

    /**
     * Роль для управления альбомами. Пользователи с этой ролью могут просматривать, создавать и редактировать альбомы.
     */
    ROLE_ALBUMS
}

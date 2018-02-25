package com.drsaina.mars.testnotification.features.ChatRoom;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drsaina.mars.testnotification.General;
import com.drsaina.mars.testnotification.Data.remot.Model.MessagesView;
import com.drsaina.mars.testnotification.Interface.OnUpdateDuration;
import com.drsaina.mars.testnotification.R;
import com.drsaina.mars.testnotification.Utill.CircleImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by mars on 5/9/2017.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private List <MessagesView> listData;
    private Context context;
    private ChatRoomAdapterListener listener;
    private int currentSoundPlay;
    private int widthDp;

    public ChatRoomAdapter ( Context context, List <MessagesView> dataList, ChatRoomAdapterListener listener, int widthDp ) {
        this.listData = dataList;
        this.context = context;
        this.listener = listener;
        this.widthDp = widthDp;
    }

    public void addMessages ( List <MessagesView> dataList ) {
        this.listData.addAll ( dataList );
    }

    public void AddItem ( MessagesView message ) {
        listData.add ( 0, message );
        notifyDataSetChanged ( );
    }

    public MessagesView getMessage ( int position ) {
        return listData.get ( position );
    }

//    public void updateStateMessage(int id, String newFileName, String state) {
//        int i = 0;
//        for (MessagesView item : listData) {
//            if (item.getMessages ().getMessageId () == id) {
//
//                if (item.getMessages().getMessageType().equals("sound")) {
//                    item.getMessages().setMessagePath (General.DIR_APP_Sound + "/" + newFileName + ".aac";);
//                    item.setStateDownload("Download");
//                }
//                item.getMessages().setMessageState(state);
//                notifyItemChanged(i);
//
//                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.sendmessage);
//                mediaPlayer.start();
//
//                break;
//            }
//            i++;
//        }
//    }

    public void pauseSound ( ) {
        currentSoundPlay = - 1;
        notifyDataSetChanged ( );
    }

    public void updateStateDownload ( int messageId, String state ) {
        int i = 0;

        for ( MessagesView item : listData ) {
            if ( item.getMessages ( ).getMessageId ( ) == messageId ) {
                item.setStateDownload ( state );
                item.getMessages ( ).setMessageState ( state );
                if ( state.equals ( "Download" ) )
                    item.getMessages ( ).setMessagePath ( General.DIR_APP + "/" + item.getMessages ( ).getMessageId ( ) + ".aac" );
                notifyItemChanged ( i );
                break;
            }
            i++;
        }
    }

    public void updateSeekBar ( int position, int value, String info ) {
        MessagesView messagesView = listData.get ( position );
        messagesView.getAudioInfo ( ).setSeekbarValue ( value );
        messagesView.getAudioInfo ( ).setTotalTime ( info );
        notifyItemChanged ( position );
    }

    public void updateStatePlay ( int position, String state ) {
        if ( state.equals ( "play" ) && position==-1 ) {
            int i = 0;
            for ( MessagesView item : listData ) {
                if ( item.getMessages ( ).getMessageType ( ).equals ( "audio" ) ) {
                    item.getAudioInfo ( ).setStatePlay ( state );
                    notifyItemChanged ( i );
                    i++;
                }
            }
            return;
        }
        MessagesView messagesView = listData.get ( position );
        messagesView.getAudioInfo ( ).setStatePlay ( state );
        notifyItemChanged ( position );
    }

    public void clear ( ) {
        this.listData.clear ( );
    }

    public class BaseHolder extends RecyclerView.ViewHolder {
        LinearLayout LinearLayoutContextMessage;
        LinearLayout LinearLayoutContextMsg;

        LinearLayout.LayoutParams params;

        TextView datemessage;
        ImageView imgMessageStatus;
//        int widthDp;

        CircleImageView avatar;

        public BaseHolder ( View itemView, int widthDp ) {
            super ( itemView );
//            this.widthDp = widthDp;
        }

        public void configView ( boolean isForMe ) {

            LinearLayoutContextMsg.setMinimumWidth ( widthDp );

            if ( ! isForMe ) {
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    LinearLayoutContextMsg.setBackground ( context.getDrawable ( R.drawable.message_border_admin ) );
                } else if ( SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                    LinearLayoutContextMsg.setBackground ( ContextCompat.getDrawable ( context, R.drawable.message_border_admin ) );
                }

                params = new LinearLayout.LayoutParams ( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                LinearLayout.LayoutParams params2 = params;

                params.gravity = Gravity.LEFT;
                params.setMargins ( 5, 5, 30, 5 );
                LinearLayoutContextMessage.setLayoutParams ( params );

                params2.gravity = Gravity.LEFT;
                LinearLayoutContextMsg.setLayoutParams ( params2 );
                avatar.setVisibility ( View.VISIBLE );

            } else {
                LinearLayoutContextMessage.setGravity ( Gravity.RIGHT );
                params = new LinearLayout.LayoutParams ( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                LinearLayout.LayoutParams params2 = params;

                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    LinearLayoutContextMsg.setBackground ( context.getDrawable ( R.drawable.message_border_user ) );
                } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ) {
                    LinearLayoutContextMsg.setBackground ( ContextCompat.getDrawable ( context, R.drawable.message_border_user ) );
                }

                params.gravity = Gravity.RIGHT;
                params.setMargins ( 30, 5, 5, 5 );
                LinearLayoutContextMessage.setLayoutParams ( params );
                params2.gravity = Gravity.RIGHT;
                LinearLayoutContextMsg.setLayoutParams ( params2 );
                avatar.setVisibility ( View.GONE );
            }
        }
    }

    public class dateMessageHolder extends RecyclerView.ViewHolder {
        TextView textDate;

        public dateMessageHolder ( View itemView ) {
            super ( itemView );
            textDate = ( TextView ) itemView.findViewById ( R.id.datemessage );
        }
    }

    public class imageHolder extends BaseHolder {
        ImageView ImageViewChat;
        ProgressBar loader;

        public imageHolder ( View view, int widthDp ) {
            super ( view, widthDp );
            avatar = view.findViewById ( R.id.avatar );

            LinearLayoutContextMessage = view.findViewById ( R.id.LinearLayoutContextMessage );
            LinearLayoutContextMsg = view.findViewById ( R.id.LinearLayoutContextMsg );
            LinearLayoutContextMessage.setGravity ( Gravity.RIGHT );

            params = new LinearLayout.LayoutParams ( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

            ImageViewChat = view.findViewById ( R.id.ImageViewChat );
            imgMessageStatus = view.findViewById ( R.id.imgMessageStatus );
            datemessage = view.findViewById ( R.id.datemessage );
            loader = view.findViewById ( R.id.Loader );
        }
    }

    public class soundHolder extends BaseHolder {

        TextView txtinfoSound;
        AppCompatSeekBar seekBarplay;

        ImageButton btnplaysound;

        ProgressBar progressBarSound;


        public soundHolder ( View view, int widthDp ) {
            super ( view, widthDp );
            avatar = view.findViewById ( R.id.avatar );

            LinearLayoutContextMessage = ( LinearLayout ) view.findViewById ( R.id.LinearLayoutContextMessage );
            LinearLayoutContextMsg = ( LinearLayout ) view.findViewById ( R.id.LinearLayoutContextMsg );
            LinearLayoutContextMessage.setGravity ( Gravity.RIGHT );

            params = new LinearLayout.LayoutParams ( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
            configView ( true );

            txtinfoSound = ( TextView ) view.findViewById ( R.id.txtinfoSound );
            btnplaysound = ( ImageButton ) view.findViewById ( R.id.btnplaysound );
            seekBarplay = ( AppCompatSeekBar ) view.findViewById ( R.id.seekbarplay );
            seekBarplay.setEnabled ( false );
            seekBarplay.setMax ( 100 );

            progressBarSound = ( ProgressBar ) view.findViewById ( R.id.progressBarSound );


            imgMessageStatus = ( ImageView ) view.findViewById ( R.id.imgMessageStatus );
            datemessage = ( TextView ) view.findViewById ( R.id.datemessage );

        }
    }

    public class textHolder extends BaseHolder {
        TextView userChatMessage;

        public textHolder ( View view, int widthDp ) {
            super ( view, widthDp );
            avatar = view.findViewById ( R.id.avatar );

            LinearLayoutContextMessage = ( LinearLayout ) view.findViewById ( R.id.LinearLayoutContextMessage );
            LinearLayoutContextMsg = ( LinearLayout ) view.findViewById ( R.id.LinearLayoutContextMsg );
            LinearLayoutContextMessage.setGravity ( Gravity.RIGHT );
            params = new LinearLayout.LayoutParams ( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );

            datemessage = view.findViewById ( R.id.datemessage );
            configView ( true );

            userChatMessage = itemView.findViewById ( R.id.userChatMessage );
            imgMessageStatus = itemView.findViewById ( R.id.imgMessageStatus );
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder ( ViewGroup parent, int viewType ) {

        View itemView;

        if ( viewType == General.ChatContenType.Sound.ordinal ( ) ) {
            itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.activity_chat_room_row_sound, parent, false );
            return new soundHolder ( itemView, widthDp );
        } else if ( viewType == General.ChatContenType.Images.ordinal ( ) ) {
            itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.activity_chat_room_row_pic, parent, false );
            return new imageHolder ( itemView, widthDp );
        } else if ( viewType == General.ChatContenType.DateMessage.ordinal ( ) ) {
            itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.activity_chat_room_row_date, parent, false );
            return new dateMessageHolder ( itemView );
        } else {
            itemView = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.activity_chat_room_row, parent, false );
            return new textHolder ( itemView, widthDp );
        }
    }

    @Override
    public void onBindViewHolder ( RecyclerView.ViewHolder holder, int position ) {

        if ( ! listData.isEmpty ( ) ) {
            final MessagesView messageChat = listData.get ( position );

            if ( holder instanceof soundHolder ) {

                final soundHolder soundHolder = ( soundHolder ) holder;

                soundHolder.progressBarSound.setProgress ( 0 );
                soundHolder.progressBarSound.setMax ( 99 );

                if ( messageChat.getAudioInfo ( ) != null ) {
                    soundHolder.txtinfoSound.setText ( messageChat.getAudioInfo ( ).getTotalTime ( ) );
                    soundHolder.seekBarplay.setProgress ( messageChat.getAudioInfo ( ).getSeekbarValue ( ) );
                }
                //state icon
                switch (messageChat.getAudioInfo ( ).getStatePlay ( )) {
                    case "stop":
                        soundHolder.btnplaysound.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_pause_button_svg ) );
                        soundHolder.btnplaysound.setTag ( "stop" );
                        soundHolder.progressBarSound.setVisibility ( View.GONE );
                        break;
                    case "play":
                        soundHolder.btnplaysound.setVisibility ( View.VISIBLE );
                        soundHolder.btnplaysound.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_play_button ) );
                        soundHolder.btnplaysound.setTag ( "play" );
                        soundHolder.progressBarSound.setVisibility ( View.GONE );
                        break;
                    case "load":
                        soundHolder.progressBarSound.setVisibility ( View.VISIBLE );
                        soundHolder.btnplaysound.setVisibility ( View.GONE );
                        soundHolder.btnplaysound.setOnClickListener ( null );
                        soundHolder.btnplaysound.setTag ( "load" );
                        break;
                    case "download":
                        soundHolder.progressBarSound.setVisibility ( View.GONE );
                        soundHolder.btnplaysound.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_download_button ) );
                        soundHolder.btnplaysound.setTag ( "download" );
                        break;
                }

                if ( messageChat.getMessages ( ).getUser ( ).getImage ( ) != null )
                    Glide.with ( context ).load ( messageChat.getMessages ( ).getUser ( ).getImage ( ) ).into ( soundHolder.avatar );

                soundHolder.datemessage.setText ( messageChat.getMessages ( ).getMessageDate ( ) );

                if ( messageChat.isForMe ( ) ) {
                    if ( messageChat.getMessages ( ).getMessageState ( ).equals ( "read" ) ) {
                        soundHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        soundHolder.progressBarSound.setVisibility ( View.GONE );
                        soundHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_ok ) );
                    } else if ( messageChat.getMessages ( ).getMessageState ( ).equals ( "send" ) ) {
                        soundHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        soundHolder.progressBarSound.setVisibility ( View.GONE );
                        soundHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_ok_server ) );
                    } else {
                        soundHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        soundHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_waite ) );
                    }
                } else {
                    soundHolder.imgMessageStatus.setVisibility ( View.GONE );
                }

                soundHolder.btnplaysound.setOnClickListener ( new View.OnClickListener ( ) {
                    @Override
                    public void onClick ( View view ) {
                        Object tag = soundHolder.btnplaysound.getTag ( );
                        if ( tag != null ) {
                            String state = ( String ) tag;
                            switch (state) {
                                case "stop":
                                    currentSoundPlay = - 1;
                                    listener.pauseSound ( );
                                    break;
                                case "play":
                                    currentSoundPlay = messageChat.getMessages ( ).getMessageId ( );
                                    listener.playSound ( listData.get ( position ).getMessages ( ).getMessagePath ( ), position );
                                    break;
                                case "load":
                                    break;
                                case "download":
                                    listener.DownloadFile ( position, messageChat, size -> {
                                        soundHolder.txtinfoSound.setText ( size );
                                    } );
                                    break;
                            }
                        }


                    }
                } );

                soundHolder.configView ( messageChat.isForMe ( ) );
            } else if ( holder instanceof textHolder ) {
                textHolder textHolder = ( textHolder ) holder;
                textHolder.configView ( messageChat.isForMe ( ) );

                if ( messageChat.getMessages ( ).getUser ( ).getImage ( ) != null )
                    Glide.with ( context ).load ( messageChat.getMessages ( ).getUser ( ).getImage ( ) ).into ( textHolder.avatar );

                if ( messageChat.getMessages ( ) != null && messageChat.getMessages ( ).getMessageContext ( ) != null )
                    textHolder.userChatMessage.setText ( Html.fromHtml ( messageChat.getMessages ( ).getMessageContext ( ) ) );
                textHolder.datemessage.setText ( messageChat.getMessages ( ).getMessageDate ( ) );

                if ( messageChat.isForMe ( ) ) {

                    if ( messageChat.getMessages ( ).getMessageState ( ) != null && messageChat.getMessages ( ).getMessageState ( ).equals ( "read" ) ) {
                        textHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        textHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_ok ) );
                    } else if ( ( messageChat.getMessages ( ).getMessageState ( ) != null && messageChat.getMessages ( ).getMessageState ( ).equals ( "send" ) ) ) {
                        textHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        textHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_ok_server ) );
                    } else {
                        textHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        textHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_waite ) );
                    }
                } else
                    textHolder.imgMessageStatus.setVisibility ( View.GONE );

                if ( messageChat.getMessages ( ).getMessageDate ( ) != null )
                    textHolder.datemessage.setText ( messageChat.getMessages ( ).getMessageDate ( ) );

            } else if ( holder instanceof imageHolder ) {
                imageHolder imageHolder = ( imageHolder ) holder;
                imageHolder.configView ( messageChat.isForMe ( ) );

                if ( messageChat.getMessages ( ).getUser ( ).getImage ( ) != null )
                    Glide.with ( context ).load ( messageChat.getMessages ( ).getUser ( ).getImage ( ) ).into ( imageHolder.avatar );

//                imageHolder.txtcaptionimagechat.setText ( Html.fromHtml ( messageChat.getMessages ( ).getMessageContent ( ) ) );
                imageHolder.datemessage.setText ( messageChat.getMessages ( ).getMessageDate ( ) );

                if ( messageChat.isForMe ( ) ) {
                    if ( messageChat.getMessages ( ).getMessageState ( ) != null && messageChat.getMessages ( ).getMessageState ( ).equals ( "read" ) ) {
                        imageHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        imageHolder.loader.setVisibility ( View.GONE );
                        imageHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_ok_image ) );

                    } else if ( messageChat.getMessages ( ).getMessageState ( ) != null && messageChat.getMessages ( ).getMessageState ( ).equals ( "send" ) ) {
                        imageHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        imageHolder.loader.setVisibility ( View.GONE );
                        imageHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_ok_server_image ) );
                    } else {
                        imageHolder.imgMessageStatus.setVisibility ( View.VISIBLE );
                        imageHolder.loader.setVisibility ( View.VISIBLE );
                        imageHolder.imgMessageStatus.setImageDrawable ( context.getResources ( ).getDrawable ( R.drawable.ic_message_waite_image ) );

                    }
                } else {
                    imageHolder.imgMessageStatus.setVisibility ( View.GONE );
                }

                if ( ! messageChat.getMessages ( ).getMessagePath ( ).contains ( "http" ) ) {
                    File file = new File ( messageChat.getMessages ( ).getMessagePath ( ) );
                    Picasso.with ( context )
                           .load ( file )
                           .resize ( 300, 300 )
                           .into ( imageHolder.ImageViewChat, new Callback ( ) {
                               @Override
                               public void onSuccess ( ) {
                               }

                               @Override
                               public void onError ( ) {
                               }
                           } );
                } else {
                    Picasso.with ( context )
                           .load ( messageChat.getMessages ( ).getMessagePath ( ) )
                           .resize ( 300, 300 )
                           .into ( imageHolder.ImageViewChat, new Callback ( ) {
                               @Override
                               public void onSuccess ( ) {
                                   imageHolder.loader.setVisibility ( View.GONE );
                               }

                               @Override
                               public void onError ( ) {

                               }
                           } );
                }

                imageHolder.ImageViewChat.setOnClickListener ( v -> listener.zoomImage ( messageChat ) );
            } else if ( holder instanceof dateMessageHolder ) {
                dateMessageHolder dateMessageHolder = ( ChatRoomAdapter.dateMessageHolder ) holder;
                dateMessageHolder.textDate.setText ( listData.get ( position ).getMessages ( ).getMessageDate ( ) );
            }
        }
    }

    @Override
    public int getItemViewType ( int position ) {
        MessagesView chat = listData.get ( position );

        if ( chat.getMessages ( ).getMessageType ( ) != null && chat.getMessages ( ).getMessageType ( ).equals ( "date" ) )
            return General.ChatContenType.DateMessage.ordinal ( );

        if ( chat.getMessages ( ).getMessageType ( ) != null && chat.getMessages ( ).getMessageType ( ).equals ( "audio" ) )
            return General.ChatContenType.Sound.ordinal ( );

        else if ( chat.getMessages ( ).getMessageType ( ) != null && chat.getMessages ( ).getMessageType ( ).equals ( "image" ) )
            return General.ChatContenType.Images.ordinal ( );

        return General.ChatContenType.Text.ordinal ( );
    }

    @Override
    public int getItemCount ( ) {
        return listData.size ( );
    }

}
package com.example.naresh.demoproject_1.fragments;/*
package com.example.naresh.demoproject_1.fragments;

*/
/**
 * Created by naresh on 28/3/17.
 *//*
public class TagDrawerFragment extends Fragment {
        private Spinner spinnerTagSearch;
        private EditText editTagSearch;
        private ListView listTag;
        private TextView textLoading;
        private ImageButton imageCancel;
        private ProgressBar progressBar;
        private View footerView;
        private TagAdapter tagAdapter;
        private int mTagPageCount = 1;
        private static final String TAG = "Tag Detail";
        private boolean isTagLoading = false;
        private String[] tagArray;
        private String tagSortBy = "popular";
        private String inname = null;
        private boolean hasMoreTag = true;


        public static TagDrawerFragment newInstance() {
            TagDrawerFragment tagDrawerFragment=new TagDrawerFragment();
            return tagDrawerFragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_tag_drawer, container, false);
            spinnerTagSearch = (Spinner) view.findViewById(R.id.spinner_tag_search);
            editTagSearch = (EditText) view.findViewById(R.id.edit_tag_search);
            listTag = (ListView) view.findViewById(R.id.list_tag);
            textLoading = (TextView) view.findViewById(R.id.text_loading);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            imageCancel = (ImageButton) view.findViewById(R.id.image_cancel);

            footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.footer_layout, null, false);
            footerView.setVisibility(View.GONE);
            listTag.addFooterView(footerView);

            tagAdapter = new TagAdapter(getActivity());
            listTag.setAdapter(tagAdapter);

            ArrayAdapter<String> tagSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item);
            tagArray = getResources().getStringArray((R.array.tag_spinner_array));
            tagSpinnerAdapter.addAll(tagArray);
            tagSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTagSearch.setAdapter(tagSpinnerAdapter);

            getJsonTagResponse();


            spinnerTagSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    tagSortBy = tagArray[position];
                    mTagPageCount = 1;
                    tagAdapter.removeItems();
                    getJsonTagResponse();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            imageCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTagSearch.getText().clear();
                    mTagPageCount = 1;
                    inname = null;
                    tagAdapter.removeItems();
                    Utils.closeKeyBoard(editTagSearch);
                    getJsonTagResponse();
                }
            });
            editTagSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        mTagPageCount = 1;
                        inname = editTagSearch.getText().toString().toLowerCase();
                        tagAdapter.removeItems();
                        Utils.closeKeyBoard(editTagSearch);
                        getJsonTagResponse();
                        return true;
                    }
                    return false;
                }
            });

            editTagSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count != 0) {
                        imageCancel.setVisibility(View.VISIBLE);

                    } else {
                        imageCancel.setVisibility(View.GONE);
                        Utils.closeKeyBoard(editTagSearch);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listTag.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount
                            && (totalItemCount - 1) != 0
                            && !isTagLoading
                            && hasMoreTag) {
                        mTagPageCount = mTagPageCount + 1;
                        getJsonTagResponse();
                    }
                }
            });
            listTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String tagname=tagAdapter.getItem(position).getName();
                    Intent intentQuestionListActivity = QuestionListActivity.getStartIntent(getActivity(),tagname);
                    startActivity(intentQuestionListActivity);
                }
            });
            return view;
        }



        private void getJsonTagResponse() {
            isTagLoading = true;
            showProgressBar();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ListResponse<TagItem>> call = apiService.getTagList(mTagPageCount,
                    Constants.VALUE_ASC, tagSortBy,
                    inname, Constants.VALUE_STACKOVER_FLOW);
            call.enqueue(new Callback<ListResponse<TagItem>>() {
                @Override
                public void onResponse(Call<ListResponse<TagItem>> call,
                                       Response<ListResponse<TagItem>> response) {
                    hideProgressBar();
                    isTagLoading = false;
                    if (response.body() != null) {
                        hasMoreTag = response.body().isHasMore();
                        tagAdapter.addItems(response.body().getItems());
                    }
                }

                @Override
                public void onFailure(Call<ListResponse<TagItem>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
        }

        private void hideProgressBar() {
            if (mTagPageCount == 1) {
                textLoading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            } else {
                footerView.setVisibility(View.GONE);
            }
        }

        private void showProgressBar() {
            if (mTagPageCount == 1) {
                textLoading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                footerView.setVisibility(View.VISIBLE);
            }
        }
    }
}
*/
